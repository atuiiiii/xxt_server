package bid.xiaocha.xxt_server.rest_controller;

import java.util.LinkedList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bid.xiaocha.xxt_server.entities.AddressEntity;
import bid.xiaocha.xxt_server.repositories.AddressRepository;

@RestController
public class AddressController {
	@Autowired
	private AddressRepository addressRepository;
	
	@RequestMapping(value="/createOrUpdateAddress",method=RequestMethod.POST)
	public AddressEntity createOrUpdateAddress(@RequestBody AddressEntity addressEntity) {
		try {
			return addressRepository.saveAndFlush(addressEntity);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value="/getAddressList/{userId}",method=RequestMethod.GET)
	@PreAuthorize("hasRole('USER') && principal.equals(#userId)")
	public List<AddressEntity> getAddressList(@PathVariable("userId")String userId ){
		//return addressRepository.findByUserId(userId);
		
		
		return new LinkedList<>();
	}
	
	@RequestMapping(value="/deleteAddress",method=RequestMethod.POST)
	public boolean deleteAddress(@RequestBody Long addressId) {
		try {
			addressRepository.delete(addressId);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
}
