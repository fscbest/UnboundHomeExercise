package com.romang.homeexercise.unbound.controllers;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SignatureException;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.romang.homeexercise.unbound.model.CRPGSignDTO;
import com.romang.homeexercise.unbound.services.CRPGRestService;
import com.romang.homeexercise.unbound.util.ErrorMessages;
import com.romang.homeexercise.unbound.util.Validator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/key")
@Api(value = "Cryptographic Sign REST API - CRPGRestAPI")
public class CRPGRestController {

	@Autowired
	CRPGRestService service;

	@ApiOperation(value = "Generate new RSA Key Pair")
	@PostMapping("/generate")
	public ResponseEntity<?> generateNewRSAKeyPair() {
		try {
			UUID id = service.generateNewRSAKeyPair();
			return ResponseEntity.status(HttpStatus.CREATED).body(id);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@ApiOperation(value = "Retern the list of all existing KeyIDs")
	@GetMapping("/list")
	public ResponseEntity<Set<UUID>> fetchRSAKeyList() {
		Set<UUID> keyList = service.fetchKeyList();
		if (null == keyList || keyList.size() < 1) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(keyList);
		}

	}

	@ApiOperation(value = "Sign on a given data using the given\n"
			+ " KeyID")
	@PostMapping("/sign/{id}")
	public ResponseEntity<?> signById(
			@PathVariable("id") @Pattern(regexp = Validator.UUID_PATTERN, message = ErrorMessages.INVALID_KEY_ID) UUID id,
			@RequestBody CRPGSignDTO signData) {
		System.out.println("	public ResponseEntity<String> signById(\n" + ": " + signData);
		if (null == id) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.INVALID_KEY_ID_NULL);
		}

		if (null == service.fetchKeyPairById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.INVALID_KEY_ID_NOT_FOUND);
		}

		try {
			return ResponseEntity.status(HttpStatus.OK).body(service.signDataByKeyId(id, signData));
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@ApiOperation(value = "Verify the given signature on the given\n"
			+ " data using the given KeyID")
	@GetMapping("/sign/verify/{id}")
	public ResponseEntity<?> verifySignedById(
			@PathVariable("id") @Pattern(regexp = Validator.UUID_PATTERN, message = ErrorMessages.INVALID_KEY_ID) UUID id,
			@RequestBody CRPGSignDTO signData) {
		System.out.println("verifySignedById: " + signData);
		if (null == id) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.INVALID_KEY_ID_NULL);
		}

		if (null == service.fetchKeyPairById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.INVALID_KEY_ID_NOT_FOUND);
		}

		try {
			return ResponseEntity.status(HttpStatus.OK).body(service.verifySignedDataByKeyId(id, signData));
		} catch (SignatureException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@ApiOperation(value = "Delete an existing RSA Key Pair using the given KeyID")
	@DeleteMapping("/deletebyid/{id}")
	public ResponseEntity<?> deleteRSAKeyPairById(
			@PathVariable("id") @Pattern(regexp = Validator.UUID_PATTERN, message = ErrorMessages.INVALID_KEY_ID) UUID id) {

		if (null == id) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessages.INVALID_KEY_ID_NULL);
		}

		boolean deleted = service.deleteRSAKeyPairById(id);

		if (deleted) {
			return ResponseEntity.status(HttpStatus.OK).body(id);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.INVALID_KEY_ID_NOT_FOUND);
		}
	}

}