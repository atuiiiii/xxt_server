package bid.xiaocha.xxt_server.rest_controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;
import javax.validation.constraints.Pattern.Flag;

import org.aspectj.bridge.MessageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpRequest;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mysql.fabric.xmlrpc.base.Array;

import bid.xiaocha.xxt_server.entities.ActiveOrderEntity;
import bid.xiaocha.xxt_server.entities.AddressEntity;
import bid.xiaocha.xxt_server.entities.FinishedOrderEntity;
import bid.xiaocha.xxt_server.entities.NeedServeEntity;
import bid.xiaocha.xxt_server.entities.OfferServeEntity;
import bid.xiaocha.xxt_server.entities.UserEntity;
import bid.xiaocha.xxt_server.model.GetResultByPage;
import bid.xiaocha.xxt_server.repositories.ActiveOrderRepository;
import bid.xiaocha.xxt_server.repositories.AddressRepository;
import bid.xiaocha.xxt_server.repositories.FinishedOrderRepository;
import bid.xiaocha.xxt_server.repositories.NeedServeRepository;
import bid.xiaocha.xxt_server.repositories.OfferServeRepository;
import bid.xiaocha.xxt_server.repositories.UserRepository;
import bid.xiaocha.xxt_server.utils.CommonUtils;

@RestController
public class ServeController {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NeedServeRepository needServeRepository;
	
	@Autowired
	private OfferServeRepository offerServeRepository;
	
	@Autowired
	private ActiveOrderRepository activeOrderRepository;
	
	@Autowired
	private FinishedOrderRepository finishedOrderRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@RequestMapping(value="/createNeedServe", method=RequestMethod.POST)
	public CreateNeedServeResult createNeedServe(@RequestBody NeedServeEntity needServeEntity) {
		String publishUserId = needServeEntity.getPublishUserId();
		UserEntity userEntity = userRepository.findOne(publishUserId);
		if(userEntity == null) {
			return CreateNeedServeResult.FAILED_UNKNOWN_ERROR;
		}
		
		double money= userEntity.getMoney();
		if(money<needServeEntity.getPrice()) {
			return CreateNeedServeResult.NOT_ENOUGH_MONEY;
		}
		Date date = new Date();
		needServeEntity.setPublishdate(date);

		needServeEntity.setServeId(CommonUtils.getOddNum(publishUserId,date));
		needServeEntity.setState(NeedServeEntity.START_SERVE);
		needServeRepository.save(needServeEntity);
		
		userEntity.setMoney(money-needServeEntity.getPrice());
		userRepository.save(userEntity);
		
		return CreateNeedServeResult.SUCCESS;
	}
	
	
	@RequestMapping(value="/createOfferServe", method=RequestMethod.POST)
	public CreateOfferServeResult createOfferServe(@RequestBody OfferServeEntity offerServeEntity) {
		
		
		Date date = new Date();
		offerServeEntity.setPublishdate(date);
		offerServeEntity.setScore(0);
		offerServeEntity.setScoreNum(0);
		offerServeEntity.setServeId(CommonUtils.getOddNum(offerServeEntity.getPublishUserId(),date));
		offerServeEntity.setState(OfferServeEntity.START_SERVE);
		offerServeRepository.save(offerServeEntity);
		
		
		return CreateOfferServeResult.SUCCESS;
	}
	
	@RequestMapping(value="/getNeedServesByPage",method=RequestMethod.GET)
	public GetResultByPage<NeedServeEntity> getNeedServesByPage(@RequestParam("pageNum") int pageNum,@RequestParam("whatSort") String whatSort ) {
		
		if(whatSort==null||whatSort.equals("null")) {
			whatSort="publishdate";
		}
		GetResultByPage<NeedServeEntity> result = new GetResultByPage<>();
		Page<NeedServeEntity> page =null;
		Sort sort = new Sort(Direction.DESC,whatSort);
		Pageable pageable = new PageRequest(pageNum,CommonUtils.NumInAPage,sort);

		page = needServeRepository.findByState(OfferServeEntity.START_SERVE,pageable);
		if(page.hasContent()) {
			result.setHaveMore(true);
			result.setDataList(page.getContent());
		}else {
			result.setHaveMore(false);
			result.setDataList(null);
		}
		return result;
	}
	
	@RequestMapping(value="/getMyNeedServesByPage/{pageNum}/{whatSort}/{userId}",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public GetResultByPage<NeedServeEntity> getMyNeedServesByPage(@PathVariable("pageNum") int pageNum,@PathVariable("whatSort") String whatSort,@PathVariable("userId") String userId) {
		
		if(whatSort==null||whatSort.equals("null")) {
			whatSort="publishdate";
		}
		GetResultByPage<NeedServeEntity> result = new GetResultByPage<>();
		Page<NeedServeEntity> page =null;
		Sort sort = new Sort(Direction.DESC,whatSort);
		Pageable pageable = new PageRequest(pageNum,CommonUtils.NumInAPage,sort);
		
	    page = needServeRepository.findByPublishUserId(userId,pageable);
		
		if(page.hasContent()) {
			result.setHaveMore(true);
			result.setDataList(page.getContent());
		}else {
			result.setHaveMore(false);
			result.setDataList(null);
		}
		return result;
	}
	
	@RequestMapping(value="/getOfferServesByPage",method=RequestMethod.GET)
	public GetResultByPage<OfferServeEntity> getOfferServesByPage(@RequestParam("pageNum") int pageNum,@RequestParam("whatSort") String whatSort){
		if(whatSort==null||whatSort.equals("null")) {
			whatSort="publishdate";
		}
		GetResultByPage<OfferServeEntity> result = new GetResultByPage<>();
		Page<OfferServeEntity> page =null;
		Sort sort = new Sort(Direction.DESC,whatSort);
		Pageable pageable = new PageRequest(pageNum,CommonUtils.NumInAPage,sort);
		

		page = offerServeRepository.findByState(OfferServeEntity.START_SERVE,pageable);
	
		if(page.hasContent()) {
			result.setHaveMore(true);
			result.setDataList(page.getContent());
		}else {
			result.setHaveMore(false);
			result.setDataList(null);
		}
		return result;
	}
	
	
	
	@RequestMapping(value="/getMyOfferServesByPage/{pageNum}/{whatSort}/{userId}",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public GetResultByPage<OfferServeEntity> getMyOfferServesByPage(@PathVariable("pageNum") int pageNum,@PathVariable("whatSort") String whatSort, @PathVariable("userId") String userId){
		if(whatSort==null||whatSort.equals("null")) {
			whatSort="publishdate";
		}
		GetResultByPage<OfferServeEntity> result = new GetResultByPage<>();
		Page<OfferServeEntity> page =null;
		Sort sort = new Sort(Direction.DESC,whatSort);
		Pageable pageable = new PageRequest(pageNum,CommonUtils.NumInAPage,sort);
		
	
	    page = offerServeRepository.findByPublishUserIdAndStateNot(userId, pageable, OfferServeEntity.DELECT_SERVE);

		if(page.hasContent()) {
			result.setHaveMore(true);
			result.setDataList(page.getContent());
		}else {
			result.setHaveMore(false);
			result.setDataList(null);
		}
		return result;
	}
	
	@RequestMapping(value="/getOneOfferServe",method=RequestMethod.POST)
	public OfferServeEntity GetOneOfferServe(@RequestBody String serveId) {
		 
		 return offerServeRepository.findOne(serveId);
	}
	
	@RequestMapping(value="/getOneNeedServe",method=RequestMethod.POST)
	public NeedServeEntity GetOneNeedServe(@RequestBody String serveId) {
		 return needServeRepository.findOne(serveId);
	}
	/* 0-成功
	 * 1-操作非法
	 * 2-服务器错误
	*/
	@RequestMapping(value="/updateNeedServe",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public short UpdateNeedServe(@RequestParam String userId ,@RequestParam String serveId,@RequestParam long addressId ,@RequestParam String content ,@RequestParam double price) {
		JSONObject result = new JSONObject();
		NeedServeEntity needServeEntity = needServeRepository.findOne(serveId);
		if(needServeEntity == null) {
			return 1;
		}
		AddressEntity addressEntity = addressRepository.findOne(addressId);
		if(addressEntity == null) {
			return 1;
		}
		UserEntity userEntity = userRepository.findOne(userId);
		if(userEntity == null) {
			return 1;
		}
		double money = userEntity.getMoney();
		userEntity.setMoney(money+needServeEntity.getPrice()-price);
		try {
			userRepository.saveAndFlush(userEntity);
		} catch (Exception e) {
			// TODO: handle exception
			return 2;
		}
		needServeEntity.setContent(content);
		needServeEntity.setLatitude(addressEntity.getLatitude());
		needServeEntity.setLongitude(addressEntity.getLongitude());
		needServeEntity.setUserName(addressEntity.getUserName());
		needServeEntity.setPhone(addressEntity.getPhone());
		needServeEntity.setPlace(addressEntity.getPlace());
		needServeEntity.setPrice(price);
		try {
			needServeRepository.saveAndFlush(needServeEntity);
			
		} catch (Exception e) {
			// TODO: handle exception
			userEntity.setMoney(money);
			userRepository.saveAndFlush(userEntity);
			return 2;
			
		}
		return 0;
		
	}
	/* 0-成功
	 * 1-操作非法
	 * 2-服务器错误
	*/
	@RequestMapping(value="/updateOfferServe",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public short UpdateOfferServe(@RequestParam String userId ,@RequestParam String serveId,@RequestParam long addressId ,@RequestParam String content ,@RequestParam double price) {
		JSONObject result = new JSONObject();
		OfferServeEntity offerServeEntity = offerServeRepository.findOne(serveId);
		if(offerServeEntity == null) {
			return 1;
		}
		AddressEntity addressEntity = addressRepository.findOne(addressId);
		if(addressEntity == null) {
			return 1;
		}
		offerServeEntity.setContent(content);
		offerServeEntity.setLatitude(addressEntity.getLatitude());
		offerServeEntity.setLongitude(addressEntity.getLongitude());
		offerServeEntity.setUserName(addressEntity.getUserName());
		offerServeEntity.setPhone(addressEntity.getPhone());
		offerServeEntity.setPlace(addressEntity.getPlace());
		offerServeEntity.setPrice(price);
		try {
			offerServeRepository.saveAndFlush(offerServeEntity);
			
		} catch (Exception e) {
			// TODO: handle exception
			return 2;
			
		}
		return 0;
	}
	
	/* 0-成功
	 * 1-操作非法
	 * 2-服务器错误
	*/
	@RequestMapping(value="/startOfferServe",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String startOfferServe(@RequestParam String userId, @RequestParam String serveId) {
		JSONObject result = new JSONObject();
		OfferServeEntity offerServeEntity = offerServeRepository.findOne(serveId);
		
		if(offerServeEntity == null) {
			result.put("result", "1");
			return result.toString();
		}
		if(!offerServeEntity.getPublishUserId().equals(userId)) {
			result.put("result", "1");
			return result.toString();
		}
		if(offerServeEntity.getState() == OfferServeEntity.START_SERVE || offerServeEntity.getState() == OfferServeEntity.DELECT_SERVE) {
			result.put("result", "1");
			return result.toString();
		}
		try {
			offerServeEntity.setState(OfferServeEntity.START_SERVE);
			offerServeRepository.saveAndFlush(offerServeEntity);
		} catch (Exception e) {
			// TODO: handle exception
			result.put("result", "2");
			return result.toString();
		}
		result.put("result", "0");
		result.put("serveEntity", offerServeEntity);
		
		return result.toJSONString();
	}
	/* 0-成功
	 * 1-操作非法
	 * 2-服务器错误
	*/
	@RequestMapping(value="/stopOfferServe",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String stopOfferServe(@RequestParam String userId,@RequestParam String serveId) {
		JSONObject result = new JSONObject();
		OfferServeEntity offerServeEntity = offerServeRepository.findOne(serveId);
		if(offerServeEntity == null) {
			result.put("result", "1");
			return result.toString();
		}
		if(!offerServeEntity.getPublishUserId().equals(userId)) {
			result.put("result", "1");
			return result.toString();
		}
		if(offerServeEntity.getState() == OfferServeEntity.STOP_SERVE || offerServeEntity.getState() == OfferServeEntity.DELECT_SERVE) {
			result.put("result", "1");
			return result.toString();
		}
		try {
			offerServeEntity.setState(OfferServeEntity.STOP_SERVE);
			offerServeRepository.saveAndFlush(offerServeEntity);
		} catch (Exception e) {
			// TODO: handle exception
			result.put("result", "2");
			return result.toString();
		}
		result.put("result", "0");
		result.put("serveEntity", offerServeEntity);
		System.out.println(result.toJSONString());
		return result.toJSONString();
	}
	/* 0-成功
	 * 1-操作非法
	 * 2-服务器错误
	*/
	@RequestMapping(value="/deleteOfferServe",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String deleteOfferServe(@RequestParam String userId,@RequestParam String serveId) {
		JSONObject result = new JSONObject();
		OfferServeEntity offerServeEntity = offerServeRepository.findOne(serveId);
		if(offerServeEntity == null) {
			result.put("result", "1");
			return result.toString();
		}
		if(!offerServeEntity.getPublishUserId().equals(userId)) {
			result.put("result", "1");
			return result.toString();
		}
		if(offerServeEntity.getState() == OfferServeEntity.DELECT_SERVE) {
			result.put("result", "1");
			return result.toString();
		}
			
		ActiveOrderEntity activeOrderEntity = activeOrderRepository.findByServeId(serveId);		
		if(activeOrderEntity == null) {
			FinishedOrderEntity finishedOrderEntity = finishedOrderRepository.findByServeId(serveId);
			if(finishedOrderEntity == null) {
				try {
					offerServeRepository.delete(serveId);
					result.put("result", "0");
					return result.toString();
				} catch (Exception e) {
					// TODO: handle exception
					result.put("result", "2");
					return result.toString();
				}
				
			}
		}
		try {
			offerServeEntity.setState(OfferServeEntity.DELECT_SERVE);
			offerServeRepository.saveAndFlush(offerServeEntity);
		} catch (Exception e) {
			// TODO: handle exception
			result.put("result", "2");
			return result.toString();
		}
	
		result.put("result", "0");
		
		return result.toJSONString();
	}
	/* 0-成功
	 * 1-操作非法
	 * 2-服务器错误
	*/
	@RequestMapping(value="/startNeedServe",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String startNeedServe(@RequestParam String userId,@RequestParam String serveId) {
		JSONObject result = new JSONObject();
		NeedServeEntity needServeEntity = needServeRepository.findOne(serveId);
		if(needServeEntity == null) {
			result.put("result", "1");
			return result.toString();
		}
		if(!needServeEntity.getPublishUserId().equals(userId)) {
			result.put("result", "1");
			return result.toString();
		}
		if(needServeEntity.getState() == NeedServeEntity.START_SERVE || needServeEntity.getState() == NeedServeEntity.DELECT_SERVE) {
			result.put("result", "1");
			return result.toString();
		}
		try {
			needServeEntity.setState(NeedServeEntity.START_SERVE);
			needServeRepository.saveAndFlush(needServeEntity);
		} catch (Exception e) {
			// TODO: handle exception
			result.put("result", "2");
			return result.toString();
		}
		
		result.put("result", "0");
		result.put("serveEntity", needServeEntity);
		
		return result.toJSONString();
	}
	/* 0-成功
	 * 1-操作非法
	 * 2-服务器错误
	*/
	@RequestMapping(value="/stopNeedServe",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String stopNeedServe(@RequestParam String userId,@RequestParam String serveId) {
		JSONObject result = new JSONObject();
		NeedServeEntity needServeEntity = needServeRepository.findOne(serveId);
		if(needServeEntity == null) {
			result.put("result", "1");
			return result.toString();
		}
		if(!needServeEntity.getPublishUserId().equals(userId)) {
			result.put("result", "1");
			return result.toString();
		}
		if(needServeEntity.getState() == NeedServeEntity.STOP_SERVE || needServeEntity.getState() == NeedServeEntity.DELECT_SERVE) {
			result.put("result", "1");
			return result.toString();
		}
		try {
			needServeEntity.setState(NeedServeEntity.STOP_SERVE);
			needServeRepository.saveAndFlush(needServeEntity);
		} catch (Exception e) {
			// TODO: handle exception
			result.put("result", "2");
			return result.toString();
		}
		result.put("result", "0");
		result.put("serveEntity", needServeEntity);
		
		return result.toJSONString();
	}
	/* 0-成功
	 * 1-操作非法
	 * 2-服务器错误
	 * 3-金钱错误
	*/
	@RequestMapping(value="/deleteNeedServe",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String deleteNeedServe(@RequestParam String userId,@RequestParam String serveId) {
		JSONObject result = new JSONObject();
		NeedServeEntity needServeEntity = needServeRepository.findOne(serveId);
		if(needServeEntity == null) {
			result.put("result", "1");
			return result.toString();
		}
		if(!needServeEntity.getPublishUserId().equals(userId)) {
			result.put("result", "1");
			return result.toString();
		}
		if(needServeEntity.getState() == NeedServeEntity.STOP_SERVE || needServeEntity.getState() == NeedServeEntity.DELECT_SERVE) {
			result.put("result", "1");
			return result.toString();
		}
		UserEntity userEntity = userRepository.findOne(needServeEntity.getPublishUserId());
		double money = userEntity.getMoney();
		try {
			userEntity.setMoney(money + needServeEntity.getPrice());
			userRepository.saveAndFlush(userEntity);
		} catch (Exception e) {
			// TODO: handle exception
			result.put("result", "2");
			return result.toString();
		}
		try {
			needServeRepository.delete(serveId);
		} catch (Exception e) {
			// TODO: handle exception
			userEntity.setMoney(money);
			userRepository.saveAndFlush(userEntity);
			result.put("result", "2");
			return result.toString();
			
		}
		
		
		
		result.put("result", "0");
		
		return result.toJSONString();
	}
	/* 0-成功
	 * 1-操作非法
	 * 2-服务器错误
	 * 3-金钱错误
	*/
	@RequestMapping(value="/getServeByServeIdList" ,method=RequestMethod.GET)
	public String getServeByServeIdList(@RequestParam String serveList) {
		 JSONObject serveIdList = JSONObject.parseObject(serveList);
		 JSONObject result = new JSONObject();
		// List<OfferServeEntity> sortServeList = getServeByServeIdList(serveIdList.get("serveList"));
		 return null;
	}
	
	public  List<OfferServeEntity> getServeByServeIdList(List<String> serveIdList) {
		 List<OfferServeEntity> serveList = offerServeRepository.findByServeIdIn(serveIdList);
		 List<OfferServeEntity> sortServeList = new ArrayList<>();
		 for(String serveId: serveIdList) {
			 for(OfferServeEntity offerServeEntity : serveList) {
				 if(offerServeEntity.getServeId().equals(serveId)) {
					 sortServeList.add(offerServeEntity);
					 break;
				 }
			 }
		 }
		 return sortServeList;
	}
	
	
	enum CreateNeedServeResult{
        SUCCESS,
        NOT_ENOUGH_MONEY,
        FAILED_UNKNOWN_ERROR,
        FAILED_NETWORK_ERROR
    }
	
	
	enum CreateOfferServeResult{
		    SUCCESS,
	        FAILED_UNKNOWN_ERROR,
	        FAILED_NETWORK_ERROR
	}
	
	
	
	
	
	
	
	
	
	
}
