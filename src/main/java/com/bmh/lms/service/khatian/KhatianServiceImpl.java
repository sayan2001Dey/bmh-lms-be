package com.bmh.lms.service.khatian;

import com.bmh.lms.dto.khatian.KhatianDTO;
import com.bmh.lms.dto.mouza.MouzaDTO;
import com.bmh.lms.model.Khatian;
import com.bmh.lms.model.KhatianNo;
import com.bmh.lms.model.Mouza;
import com.bmh.lms.model.MouzaCollection;
import com.bmh.lms.repository.KhatianNoRepository;
import com.bmh.lms.repository.KhatianRepository;
import com.bmh.lms.service.utils.CommonUtils;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class KhatianServiceImpl implements  KhatianService{

    @Autowired
    private KhatianRepository repository;

    @Autowired
    private KhatianNoRepository khatianNoRepository;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    public KhatianDTO createKhatianMaster(KhatianDTO khatianDTO, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Pair<Khatian, List<KhatianNo>> precessedData =  khatianDTOToData(khatianDTO);
        Khatian khatian = precessedData.a;
        List<KhatianNo> khatianNos = precessedData.b;

        // Generate and set Khatian ID
        khatian.setKhatianId(commonUtils.generateUID("Khatian", "KM"));
        khatian.setInserted_by(username);
        khatian.setModified_type("INSERTED");
        khatian.setInserted_on(ldt);
        khatian.setUpdated_by("NA");
        khatian.setUpdated_on(null);
        khatian.setDeleted_by("NA");
        khatian.setDeleted_on(null);

        // Save Khatian entity first
        Khatian savedKhatian = repository.save(khatian);

        Long tmpId = savedKhatian.getId();
        if(tmpId != null && khatianNos != null) {
            for (KhatianNo khatianNo : khatianNos) {
                khatianNo.setKhatianNoId(commonUtils.generateUID("KhatianNo", "KN"));
                khatianNo.setKhatianId(tmpId); // Set the saved Khatian in each KhatianNo entity
                khatianNo.setCurrent(true);
            }
            // Save KhatianNo entities
            khatianNoRepository.saveAll(khatianNos);
        }

        khatianDTO.setKhatianId(khatian.getKhatianId());
        return khatianDTO;
    }

    @Override
    public List<KhatianDTO> getAllKhatianMaster() {
        List<KhatianDTO> res = new ArrayList<>();
        repository.findAllActiveByKhatianId().forEach((k)-> {
            res.add(dataToKhatianDTO(k, khatianNoRepository.findKhatianNoList(k.getId())));
        });

        return res;
    }

    @Override
    public Optional<KhatianDTO> getKhatianMasterById(String khatian_id) {
        Khatian k = repository.findByKhatianId(khatian_id).orElse(null);
        return k == null ? Optional.empty() : Optional.of(dataToKhatianDTO(k, khatianNoRepository.findKhatianNoList(k.getId())));
    }

    @Override
    @Transactional
    public KhatianDTO updateKhatianMaster(String khatian_id, KhatianDTO updatedKhatianDTO, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Khatian oldK = repository.findByKhatianId(khatian_id).orElse(null);

        if (oldK == null) {
            return null;
        }

        Pair<Khatian, List<KhatianNo>> precessedData =  khatianDTOToData(updatedKhatianDTO);
        Khatian updatedKhatian = precessedData.a;
        List<KhatianNo> updatedKhatianNos = precessedData.b;

        updatedKhatian.setKhatianId(khatian_id);
        updatedKhatian.setModified_type("INSERTED");
        updatedKhatian.setInserted_by(oldK.getInserted_by());
        updatedKhatian.setInserted_on(oldK.getInserted_on());
        updatedKhatian.setUpdated_by(username);
        updatedKhatian.setUpdated_on(ldt);
        updatedKhatian.setDeleted_by("NA");
        updatedKhatian.setDeleted_on(null);

        Long tmpId = oldK.getId();
        if(tmpId != null) {
            List<KhatianNo> oldKNos = khatianNoRepository.findKhatianNoList(tmpId);
            for (KhatianNo khatianNo : oldKNos) {
                khatianNo.setCurrent(false);
            }
            khatianNoRepository.saveAll(oldKNos);
        }

        oldK.setModified_type("UPDATED");
        oldK.setUpdated_by(username);
        oldK.setUpdated_on(ldt);

        tmpId = repository.save(updatedKhatian).getId();
        if(tmpId != null && updatedKhatianNos != null) {
            for (KhatianNo khatianNo : updatedKhatianNos) {
                if(khatianNo.getKhatianNoId() == null || !khatianNo.getKhatianNoId().startsWith("KN-"))
                    khatianNo.setKhatianNoId(commonUtils.generateUID("KhatianNo", "KN"));
                khatianNo.setKhatianId(tmpId); // Set the saved Khatian in each KhatianNo entity
                khatianNo.setCurrent(true);
            }
            // Save KhatianNo entities
            khatianNoRepository.saveAll(updatedKhatianNos);
        }

        updatedKhatianDTO.setKhatianId(khatian_id);
        return updatedKhatianDTO;
    }

    @Override
    @Transactional
    public Boolean deleteKhatianMaster(String khatian_id, String username) {
        LocalDateTime ldt = LocalDateTime.now();
        Khatian khatian = repository.findByKhatianId(khatian_id).orElse(null);

        if (khatian == null) {
            return false;
        }

        Long tmpId = khatian.getId();
        if(tmpId != null) {
            List<KhatianNo> oldKNos = khatianNoRepository.findKhatianNoList(tmpId);
            for (KhatianNo khatianNo : oldKNos) {
                khatianNo.setCurrent(false);
            }
            khatianNoRepository.saveAll(oldKNos);
        }

        khatian.setModified_type("DELETED");
        khatian.setDeleted_by(username);
        khatian.setDeleted_on(ldt);

        repository.save(khatian);

        return true;
    }

    private Pair<Khatian, List<KhatianNo>> khatianDTOToData(KhatianDTO khatianDTO) {
        Khatian khatian = new Khatian();

        khatian.setKhatianId(khatianDTO.getKhatianId());
        khatian.setRemarks(khatianDTO.getRemarks());
        khatian.setWebsite(khatianDTO.getWebsite());
        khatian.setJila(khatianDTO.getJila());
        khatian.setAnchal(khatianDTO.getAnchal());
        khatian.setBlock(khatianDTO.getBlock());
        khatian.setHalka(khatianDTO.getHalka());
        khatian.setMouza(khatianDTO.getMouza());
        khatian.setKhatianType(khatianDTO.getKhatianType());
        khatian.setNameWithKhatian(khatianDTO.getNameWithKhatian());
        khatian.setDistrict(khatianDTO.getDistrict());
        khatian.setAssesseeNo(khatianDTO.getAssesseeNo());
        khatian.setReKmc(khatianDTO.getReKmc());

        List<KhatianNo> khatianNos;
        if(khatianDTO.getKhatianNos() == null)
            khatianNos = new ArrayList<>();
        else
            khatianNos = khatianDTO.getKhatianNos();

        return new Pair<>(khatian, khatianNos);
    }

    private KhatianDTO dataToKhatianDTO(Khatian khatian, List<KhatianNo> khatianNos) {
        if(khatian == null)
            return null;

        KhatianDTO res = new KhatianDTO();

        res.setKhatianId(khatian.getKhatianId());
        res.setRemarks(khatian.getRemarks());
        res.setWebsite(khatian.getWebsite());
        res.setJila(khatian.getJila());
        res.setAnchal(khatian.getAnchal());
        res.setBlock(khatian.getBlock());
        res.setHalka(khatian.getHalka());
        res.setMouza(khatian.getMouza());
        res.setKhatianType(khatian.getKhatianType());
        res.setNameWithKhatian(khatian.getNameWithKhatian());
        res.setDistrict(khatian.getDistrict());
        res.setAssesseeNo(khatian.getAssesseeNo());
        res.setReKmc(khatian.getReKmc());

        res.setKhatianNos(Objects.requireNonNullElseGet(khatianNos, ArrayList::new));

        return res;
    }

}
