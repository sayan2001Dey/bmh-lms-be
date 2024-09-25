package com.bmh.lms.service.group;

import com.bmh.lms.model.Group;
import com.bmh.lms.repository.GroupRepository;
import com.bmh.lms.service.utils.CommonUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService{

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CommonUtils commonUtils;

    @Override
    public Group createGroup(Group group, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        group.setGroupId(commonUtils.generateUID("Group", "GM"));
        group.setModified_type("INSERTED");
        group.setInserted_by(username);
        group.setInserted_on(ldt);
        group.setUpdated_by("NA");
        group.setUpdated_on(null);
        group.setDeleted_by("NA");
        group.setDeleted_on(null);

        return groupRepository.save(group);
    }

    @Override
    public List<Group> getAllGroup() {
        return groupRepository.findAllActiveByGroupId();
    }

    @Override
    public Optional<Group> getGroupById(String group_id) {

        return groupRepository.findByGroupId(group_id);
    }

    @Override
    @Transactional
    public Group updateGroup(String group_id, Group updatedGroup, String username) {
        LocalDateTime ldt = LocalDateTime.now();

        Group oldGroup = groupRepository.findByGroupId(group_id).orElse(null);

        if(oldGroup == null) return null;

        updatedGroup.setGroupId(group_id);
        updatedGroup.setModified_type("INSERTED");
        updatedGroup.setInserted_by(oldGroup.getInserted_by());
        updatedGroup.setInserted_on(oldGroup.getInserted_on());
        updatedGroup.setUpdated_by(username);
        updatedGroup.setUpdated_on(ldt);
        updatedGroup.setDeleted_by("NA");
        updatedGroup.setDeleted_on(null);

        oldGroup.setModified_type("UPDATED");
        oldGroup.setUpdated_by(username);
        oldGroup.setUpdated_on(ldt);

        groupRepository.save(oldGroup);

        return groupRepository.save(updatedGroup);
    }

    @Override
    public Boolean deleteGroup(String group_id, String username) {
        LocalDateTime ldt = LocalDateTime.now();
        Group group = groupRepository.findByGroupId(group_id).orElse(null);

        if(group == null) return false;

        group.setModified_type("DELETED");
        group.setDeleted_by(username);
        group.setDeleted_on(ldt);

        groupRepository.save(group);

        return true;
    }
}
