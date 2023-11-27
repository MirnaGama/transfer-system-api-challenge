package com.mirna.transferapi.domain.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.mirna.transferapi.domain.dtos.UserDTO;
import com.mirna.transferapi.domain.entities.User;

@Component
public class UserMapper {

	 private ModelMapper mapper;
	 
	    public UserMapper() {
	    	mapper =  new ModelMapper();
	    }
	    
	    public User toUserEntity(UserDTO userDTO) {
	        return mapper.map(userDTO, User.class);
	    }

	    public UserDTO toUserDTO(User userEntity) {
	        return mapper.map(userEntity, UserDTO.class);
	    }
	    
}
