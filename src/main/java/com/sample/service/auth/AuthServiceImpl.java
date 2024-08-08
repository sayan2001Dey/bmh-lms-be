package com.sample.service.auth;

import com.google.common.hash.Hashing;
import com.sample.dto.auth.AuthReqDTO;
import com.sample.dto.auth.AuthResDTO;
import com.sample.dto.user.UserResDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sample.model.User;
import com.sample.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	    private UserRepository userRepository;

	@Autowired
		private Environment env;

	public AuthResDTO login(AuthReqDTO authReq) {
		 String username = authReq.getUsername().toLowerCase();

		 if(username.isEmpty())
			 throw new RuntimeException("invalid credential");

		 User user = userRepository.findActiveByUsername(username).orElse(null);

		 String passwordHash = getPasswordHash(authReq.getPassword(), username);

		 if(user != null && user.getPassword().equals(passwordHash)) {
			 Boolean admin = user.getAdmin();
			 return new AuthResDTO(user.getName(), username, admin, generateToken(username, passwordHash, admin));
		 } else throw new RuntimeException("invalid credential");
     }

    public UserResDTO register(AuthReqDTO authReq, String adminUsername) {
		LocalDateTime ldt = LocalDateTime.now();
		String username = authReq.getUsername().toLowerCase().replaceAll("\\s+","");
		String password = authReq.getPassword();
		String name = authReq.getName();
		Boolean admin = authReq.getAdmin();
		admin = admin!=null && admin;

		if(username.isEmpty() || userRepository.findActiveByUsername(username).orElse(null) != null || username.charAt(0) == '_')
			throw new RuntimeException("illegal username");

		if(password.length()<8)
			throw new RuntimeException("invalid password");

		User newUser = new User();

		if(name!=null && !name.isEmpty())
        	newUser.setName(authReq.getName());

		newUser.setAdmin(admin);

		newUser.setUsername(username);

		newUser.setPassword(getPasswordHash(password, username));

		newUser.setModified_type("INSERTED");

		newUser.setInserted_on(ldt);

		newUser.setInserted_by(adminUsername);

        userRepository.save(newUser);

		return new UserResDTO(name, username, admin);
    }

	public Object[] verifyToken(String token) {
		long validity = 2629746000L;
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");

		try {
			if(token.startsWith("Bearer "))
				token = token.substring(7);
			String[] tokenParts = token.split("\\.");
			String headerStr = new String(Base64.getDecoder().decode(tokenParts[0]));
			JSONObject header = new JSONObject(headerStr);
			switch (header.getInt("version")) {
				case 1:
					String payloadStr = new String(Base64.getDecoder().decode(tokenParts[1]));
					String signature = tokenParts[2];
					JSONObject payload = new JSONObject(payloadStr);
					String username = payload.getString("username");
					long validTill = payload.getLong("validTill");
					User user = userRepository.findActiveByUsername(username).orElse(null);
					if(user == null) return null;
					String localSignature = generateSignature(
							username,
							tokenParts[0] + "." + tokenParts[1] + user.getPassword(),
							validTill - validity
						);
					long epochTimeNow = LocalDate.now().atStartOfDay(zoneId).toInstant().toEpochMilli();
					if(tokenParts[2].equals(localSignature) && validTill > epochTimeNow) {
						return new Object[]{username, payload.getBoolean("admin")};
					}
				default:
					return null;
			}
		} catch (Exception err) {
			System.out.println(err.getMessage());
		}
		return null;
	}

	private String getPasswordHash(String password, String username) {
		String input = password + env.getProperty(
				"password.salt",
				env.getProperty("spring.datasource.password", "archi")
		) + username;

		return Hashing
				.sha512()
				.hashString(input, StandardCharsets.UTF_8)
				.toString();
	}

	private String generateSignature(String username, String data, long epochTimeWhenCreated) {
		String key = env.getProperty(
				"hmac.key",
				env.getProperty(
						"password.salt",
						env.getProperty("spring.datasource.password", "archi")));

		return Hashing
				.sha512()
				.hashString(data + key + epochTimeWhenCreated + username, StandardCharsets.UTF_8)
				.toString();
	}

	private String generateToken(String username, String passwordHash, Boolean admin) {
		long validity = 2629746000L;
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");

		JSONObject header = new JSONObject();
		JSONObject payload = new JSONObject();

		header.put("version", 1);
		payload.put("username", username);

		long ldt = LocalDate.now().atStartOfDay(zoneId).toInstant().toEpochMilli();
		Long validTill = ldt + validity;

		payload.put("validTill", validTill);
		payload.put("admin", admin);

		String headerEncoded = base64RemovePadding(Base64.getEncoder().encodeToString(JSONObject.valueToString(header).getBytes()));
		String payloadEncoded = base64RemovePadding(Base64.getEncoder().encodeToString(JSONObject.valueToString(payload).getBytes()));

		String tokenObjStr = headerEncoded + "." + payloadEncoded;

		return tokenObjStr + "." + generateSignature(username, tokenObjStr + passwordHash, ldt);
	}

	private String base64RemovePadding(String b64str) {
		int i = b64str.length()-1;
		while(-1<i) {
			if(b64str.charAt(i)!='=')
				break;
			i--;
		}
		return b64str.substring(0, i+1);
	}
}
