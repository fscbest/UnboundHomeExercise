package com.romang.homeexercise.unbound.services;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.romang.homeexercise.unbound.model.CRPGSignDTO;

@Service
public class CRPGRestService {
	
	private final ConcurrentHashMap<UUID, KeyPair> keyMap = new ConcurrentHashMap<UUID, KeyPair>();

	public UUID generateNewRSAKeyPair() throws GeneralSecurityException {
		
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		UUID id;
		do {
			id = UUID.randomUUID();
		} while (null != keyMap.putIfAbsent(id, keyPair));
		
		return id;
	}
	
	public Set<UUID> fetchKeyList() {	
				
		return keyMap.keySet();
	}
	
	public KeyPair fetchKeyPairById(UUID id) {
		return keyMap.get(id);
	}
	
	public boolean deleteRSAKeyPairById(UUID id){
		return null != keyMap.remove(id) ;

	}
	
	public String signDataByKeyId(UUID id, CRPGSignDTO signData) throws GeneralSecurityException, UnsupportedEncodingException {
		
		KeyPair keyPair = fetchKeyPairById(id);
		if(null == keyPair) {
			return null;
		}
		
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(keyPair.getPrivate());
		signature.update(signData.getData().getBytes("UTF8"));
		byte[] signatureBytes = signature.sign();
        System.out.println("Signature:" + Base64.getEncoder().encodeToString(signatureBytes));
        
        return Base64.getEncoder().encodeToString(signatureBytes);
	}
	
public boolean verifySignedDataByKeyId(UUID id, CRPGSignDTO signData) throws GeneralSecurityException, UnsupportedEncodingException, SignatureException{
		
		KeyPair keyPair = fetchKeyPairById(id);
		if(null == keyPair) {
			return false;
		}
		
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initVerify(keyPair.getPublic());
		signature.update(signData.getData().getBytes("UTF8"));
		byte[] signatureBytes = Base64.getDecoder().decode(signData.getSignature());
        
        return signature.verify(signatureBytes);
	}
}
