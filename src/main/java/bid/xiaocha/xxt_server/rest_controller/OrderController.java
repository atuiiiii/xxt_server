package bid.xiaocha.xxt_server.rest_controller;

import java.util.Date;

import org.aspectj.apache.bcel.generic.RET;
import org.aspectj.apache.bcel.generic.ReturnaddressType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bid.xiaocha.xxt_server.entities.ActiveOrderEntity;
import bid.xiaocha.xxt_server.entities.FinishedOrderEntity;
import bid.xiaocha.xxt_server.entities.NeedServeEntity;
import bid.xiaocha.xxt_server.entities.OfferServeEntity;
import bid.xiaocha.xxt_server.entities.UserEntity;
import bid.xiaocha.xxt_server.model.GetResultByPage;
import bid.xiaocha.xxt_server.repositories.ActiveOrderRepository;
import bid.xiaocha.xxt_server.repositories.FinishedOrderRepository;
import bid.xiaocha.xxt_server.repositories.NeedServeRepository;
import bid.xiaocha.xxt_server.repositories.OfferServeRepository;
import bid.xiaocha.xxt_server.repositories.UserRepository;
import bid.xiaocha.xxt_server.utils.CommonUtils;


@RestController
public class OrderController {
	
	@Autowired
	private NeedServeRepository needServeRepository;
	@Autowired
	private OfferServeRepository offerServeRepository;
	
	@Autowired
	private ActiveOrderRepository activeOrderRepository;
  
	@Autowired
	private FinishedOrderRepository finishedOrderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
    public final short ACTIVE_ORDER = 1;
    public final short FINISHED_ORDER = 2;
	
	/*
	 * 成功为0
	 * 错误码1：服务已暂停或已被接单
	 * 错误码2：服务暂停失败
	 * 错误码3：订单生成失败
	 */
	@RequestMapping(value="/createNeedOrder", method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String createNeedOrder(@RequestParam String serveId,@RequestParam String userId) {
		JSONObject result = new JSONObject();
		System.out.println(serveId +"  "+userId);
		NeedServeEntity needServe = needServeRepository.findOne(serveId);
		if (needServe.getState() == NeedServeEntity.STOP_SERVE) {
			return result.put("result", 1).toString();
		}
		try {
			needServe.setState(NeedServeEntity.STOP_SERVE);
			needServeRepository.saveAndFlush(needServe);
		} catch (Exception e) {
			return result.put("result", 2).toString();
		}
		ActiveOrderEntity activeOrder = new ActiveOrderEntity();
		Date date = new Date();
		String orderId = CommonUtils.getOddNum(needServe.getPublishUserId(), date);
		activeOrder.setOrderId(orderId);
		activeOrder.setBuildDate(date);
		activeOrder.setContent(needServe.getContent());
		activeOrder.setPhone(needServe.getPhone());
		activeOrder.setPlace(needServe.getPlace());
		activeOrder.setPrice(needServe.getPrice());
		activeOrder.setServeId(serveId);
		activeOrder.setServePublisherId(needServe.getPublishUserId());
		activeOrder.setServeReceiverId(userId);
		activeOrder.setServeType(ActiveOrderEntity.NEED_SERVE);
		activeOrder.setState(ActiveOrderEntity.STATE_WAIT_AGREE_CREATE);
		activeOrder.setCancelState(ActiveOrderEntity.CANCEL_STATE_NOT_CANCEL);
		activeOrder.setTitle(needServe.getTitle());
		activeOrder.setType(needServe.getType());
		try {
			activeOrderRepository.saveAndFlush(activeOrder);
		} catch (Exception e) {
			e.printStackTrace();
			needServe.setState(NeedServeEntity.START_SERVE);
			needServeRepository.saveAndFlush(needServe);
			return result.put("result", 3).toString();
		}
		result.put("result", 0);
		result.put("orderId", orderId);
		System.out.println(result.toJSONString());
		System.out.println(result.toString());
		return result.toString();
	}
	
	/*
	 * 成功为0
	 * 错误码1：服务已暂停
	 * 错误码2：用户余额不足
	 * 错误码3：订单生成失败
	 */
	@RequestMapping(value="/createOfferOrder", method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String createOfferOrder(@RequestParam String serveId,@RequestParam String userId) {
		JSONObject jsonObject = new JSONObject();
		OfferServeEntity offerServe = offerServeRepository.findOne(serveId);
		if (offerServe.getState() == OfferServeEntity.START_SERVE) {
			return jsonObject.put("result", 1).toString();
		}
		
		UserEntity userEntity = userRepository.findOne(userId);
		if(userEntity.getMoney()<offerServe.getPrice()) {
			jsonObject.put("result", 2);
			jsonObject.put("requireMoney", offerServe.getPrice()-userEntity.getMoney());
			return jsonObject.toString();
		}
		
		ActiveOrderEntity activeOrder = new ActiveOrderEntity();
		Date date = new Date();
		String orderId = CommonUtils.getOddNum(offerServe.getPublishUserId(), date);
		activeOrder.setOrderId(orderId);
		activeOrder.setBuildDate(date);
		activeOrder.setContent(offerServe.getContent());
		activeOrder.setPhone(offerServe.getPhone());
		activeOrder.setPlace(offerServe.getPlace());
		activeOrder.setPrice(offerServe.getPrice());
		activeOrder.setServeId(serveId);
		activeOrder.setServePublisherId(offerServe.getPublishUserId());
		activeOrder.setServeReceiverId(userId);
		activeOrder.setCancelState(ActiveOrderEntity.CANCEL_STATE_NOT_CANCEL);
		activeOrder.setServeType(ActiveOrderEntity.NEED_SERVE);
		activeOrder.setState(ActiveOrderEntity.STATE_WAIT_AGREE_CREATE);
		activeOrder.setTitle(offerServe.getTitle());
		activeOrder.setType(offerServe.getType());
		double oldMoney = userEntity.getMoney();
		try {
			userEntity.setMoney(oldMoney - offerServe.getPrice());
			userRepository.saveAndFlush(userEntity);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return jsonObject.put("result", 3).toString();
		}
		try {
			activeOrderRepository.saveAndFlush(activeOrder);
		} catch (Exception e) {
			// TODO: handle exception
			userEntity.setMoney(oldMoney);
			userRepository.saveAndFlush(userEntity);
			return jsonObject.put("result", 3).toString();
		}
		jsonObject.put("result", 0);
		jsonObject.put("orderId", orderId);

		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value="/getMyActiveOrderByPage/{userId}/{pageNum}",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public GetResultByPage<ActiveOrderEntity> showMyActiveOrder(@PathVariable String userId,@PathVariable int pageNum){
		GetResultByPage<ActiveOrderEntity> result = new GetResultByPage<>();
		Sort sort = new Sort(Direction.DESC,"buildDate");
		Pageable pageable = new PageRequest(pageNum,CommonUtils.NumInAPage,sort);
		Page<ActiveOrderEntity> page = activeOrderRepository.findMyActiveOrder(userId, pageable);
		if (page.hasContent()) {
			result.setHaveMore(true);
			result.setDataList(page.getContent());
		}else {
			result.setHaveMore(false);
			result.setDataList(null);
		}
		return result;
	}
	
	
	@RequestMapping(value="/getMyFinishedOrderByPage/{userId}/{pageNum}",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public GetResultByPage<FinishedOrderEntity> showMyFinishedOrder(@PathVariable String userId,@PathVariable int pageNum){
		GetResultByPage<FinishedOrderEntity> result = new GetResultByPage<>();
		Sort sort = new Sort(Direction.DESC,"finishDate");
		Pageable pageable = new PageRequest(pageNum,CommonUtils.NumInAPage,sort);
		Page<FinishedOrderEntity> page = finishedOrderRepository.findMyFinishedOrder(userId, pageable);
		if (page.hasContent()) {
			result.setHaveMore(true);
			result.setDataList(page.getContent());
		}else {
			result.setHaveMore(false);
			result.setDataList(null);
		}
		return result;
	}
	/*
	 * 成功为0
	 * 错误码1:你没有权限查看该订单信息
	 * 错误码2:找不到该订单
	 */
	@RequestMapping(value="/getOrderByOrderId",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String getOrderByOrderId(@RequestParam String orderId,@RequestParam String userId,@RequestParam short orderType) {
		JSONObject result = new JSONObject();
		ActiveOrderEntity activeOrder = null;
		FinishedOrderEntity finishedOrder = null;
		System.out.println(orderId);
		if (orderType != FINISHED_ORDER) {
			activeOrder = activeOrderRepository.findOne(orderId);
		}
		if (activeOrder!=null) {
			if (!userId.equals(activeOrder.getServePublisherId())&&!userId.equals(activeOrder.getServeReceiverId())) {
				result.put("result",1);
				return result.toJSONString();
			}else {
				result.put("result", 0);
				result.put("activeOrder", activeOrder);
				return result.toJSONString();
			}
		}
		finishedOrder = finishedOrderRepository.findOne(orderId);
		if (finishedOrder == null) {
			result.put("result", 2);
			return result.toJSONString();
		}
		if (!userId.equals(finishedOrder.getServePublisherId())&&userId.equals(finishedOrder.getServeReceiverId())) {
			result.put("result",1);
			return result.toJSONString();
		}else {
			result.put("result", 0);
			result.put("finishedOrder", finishedOrder);
			return result.toJSONString();
		}
	}
	
	
	/*
	 * 同意接单（待接单->已接单）
	 * 成功为0
	 * 错误码1:找不到该订单
	 * 错误码2:订单状态错误（应为待接单）
	 * 错误码3:你没有权限接下订单（你不是服务的发布者）
	 * 错误码4:接单失败，提示重试
	 */
	@RequestMapping(value="/agreeCreate",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String agreeCreate(@RequestParam String orderId,@RequestParam String userId) {
		JSONObject result = new JSONObject();
		ActiveOrderEntity activeOrder = activeOrderRepository.findOne(orderId);
		if (activeOrder == null) {
			result.put("result",1);
			return result.toString();
		}
		if (activeOrder.getState()!=ActiveOrderEntity.STATE_WAIT_AGREE_CREATE) {
			result.put("result",2);
			return result.toString();
		}
		if (!userId.equals(activeOrder.getServePublisherId())) {
			result.put("result",3);
			return result.toString();
		}
		activeOrder.setState(ActiveOrderEntity.STATE_BEGIN_SERVE);
		activeOrder.setStartDate(new Date());
		try {
			activeOrderRepository.save(activeOrder);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("result",4);
			return result.toString();
		}
		result.put("result",0);
		result.put("orderEntity", activeOrder);
		return result.toString();
	}
	
	/*
	 * 服务方已完成（已接单->已完成，待确认）
	 * 成功为0
	 * 错误码1:找不到该订单
	 * 错误码2:订单状态错误（应为已接单）
	 * 错误码3:你没有权限发出完成请求
	 * 错误码4:订单完成失败，提示重试
	 */
	@RequestMapping(value="/finishOrder",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String finishOrder(@RequestParam String orderId,@RequestParam String userId) {
		JSONObject result = new JSONObject();
		ActiveOrderEntity activeOrder = activeOrderRepository.findOne(orderId);
		if (activeOrder == null) {
			result.put("result",1);
			return result.toString();
		}
		if (activeOrder.getState()!=ActiveOrderEntity.STATE_BEGIN_SERVE) {
			result.put("result",2);
			return result.toString();
		}
		if (activeOrder.getServeType() == ActiveOrderEntity.OFFER_SERVE && !userId.equals(activeOrder.getServePublisherId())) {
			result.put("result",3);
			return result.toString();
		}
		if (activeOrder.getServeType() == ActiveOrderEntity.NEED_SERVE && !userId.equals(activeOrder.getServeReceiverId())) {
			result.put("result",3);
			return result.toString();
		}
		activeOrder.setState(ActiveOrderEntity.STATE_WAIT_FOR_CONFIRM);
		activeOrder.setFinishDate(new Date());
		try {
			activeOrderRepository.save(activeOrder);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result",4);
			return result.toString();
		}
		result.put("result", 0);
		result.put("orderEntity", activeOrder);
		return result.toJSONString();
	}
	
	/*
	 * 服务方已完成（已完成，待确认->已确认）
	 * 成功为0
	 * 错误码1:找不到该订单
	 * 错误码2:订单状态错误（应为已完成，待确认）
	 * 错误码3:你没有权限发出完成请求
	 * 错误码4:找不到打钱的账号
	 * 错误码5:新订单生成失败
	 * 错误码6:旧订单删除失败
	 * 错误码7:钱没成功打到对方账号上
	 */
	@RequestMapping(value="/confrimOrder",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String confrimOrder(@RequestParam String orderId,@RequestParam String userId) {
		JSONObject result = new JSONObject();
		ActiveOrderEntity activeOrder = activeOrderRepository.findOne(orderId);
		if (activeOrder == null) {
			result.put("result",1);
			return result.toString();
		}
		System.out.println(activeOrder.getState());
		if (activeOrder.getState()!=ActiveOrderEntity.STATE_WAIT_FOR_CONFIRM) {
			result.put("result",2);
			return result.toString();
		}
		if (activeOrder.getServeType() == ActiveOrderEntity.NEED_SERVE && !userId.equals(activeOrder.getServePublisherId())) {
			result.put("result",3);
			return result.toString();
		}
		if (activeOrder.getServeType() == ActiveOrderEntity.OFFER_SERVE && !userId.equals(activeOrder.getServeReceiverId())) {
			result.put("result",3);
			return result.toString();
		}
		String giveMoneyUserId;
		if (activeOrder.getServeType() == ActiveOrderEntity.NEED_SERVE) {
			giveMoneyUserId = activeOrder.getServeReceiverId();
		}else {
			giveMoneyUserId = activeOrder.getServePublisherId();
		}
		UserEntity user = userRepository.findOne(giveMoneyUserId);
		if (user == null) {
			result.put("result",4);
			return result.toString();
		}
		user.setMoney(user.getMoney()+activeOrder.getPrice());
		
		FinishedOrderEntity finishedOrder = createFinisherOrder(activeOrder);
		finishedOrder.setFinishDate(new Date());
		finishedOrder.setState(FinishedOrderEntity.STATE_WAIT_FOR_APPRAISE);
		try {
			finishedOrderRepository.saveAndFlush(finishedOrder);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result",5);
			return result.toString();
		}
		try {
			activeOrderRepository.delete(activeOrder);
		} catch (Exception e) {
			e.printStackTrace();
			finishedOrderRepository.delete(finishedOrder);
			result.put("result",6);
			return result.toString();
		}
		try {
			userRepository.saveAndFlush(user);
		} catch (Exception e) {
			e.printStackTrace();
			activeOrderRepository.save(activeOrder);
			finishedOrderRepository.delete(finishedOrder);
			result.put("result",7);
			return result.toString();
		}
		result.put("result", 0);
		result.put("orderEntity", finishedOrder);
		return result.toJSONString();
	}
	
	
	/*
	 * 拒绝取消订单
	 * 成功为0
	 * 错误码1:找不到该订单
	 * 错误码2:订单不处于待取消状态
	 * 错误码3:你没有权限拒绝取消订单
	 * 错误码4:取消订单失败（请重试）
	 */
	@RequestMapping(value="/refuseCancelOrder",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String refuseCancelOrder(@RequestParam String orderId,@RequestParam String userId) {
		JSONObject result = new JSONObject();
		ActiveOrderEntity activeOrder = activeOrderRepository.findOne(orderId);
		if (activeOrder == null) {
			result.put("result",1);
			return result.toString();
		}
		if (activeOrder.getCancelState() == ActiveOrderEntity.CANCEL_STATE_NOT_CANCEL) {
			result.put("result",2);
			return result.toString();
		}
		if (activeOrder.getCancelState() == ActiveOrderEntity.CANCEL_STATE_PUBLISHER_HAS_CANCEL
				||activeOrder.getCancelState() == ActiveOrderEntity.CANCEL_STATE_RECEIVER_HAS_CANCEL) {
			if (!userId.equals(activeOrder.getServePublisherId())&&!userId.equals(activeOrder.getServeReceiverId())) {
				result.put("result",3);
				return result.toString();
			}
		}
		activeOrder.setCancelState(ActiveOrderEntity.CANCEL_STATE_NOT_CANCEL);
		try {
			activeOrderRepository.save(activeOrder);
		} catch (Exception e) {
			result.put("result",4);
			return result.toString();
		}
		result.put("result",0);
		result.put("orderEntity",activeOrder);
		return result.toJSONString();
	}
	
	
	/*
	 * 取消订单
	 * 成功为0
	 * 错误码1:保存取消信息失败（双方为取消转为单方取消情况）
	 * 错误码2:服务重新挂起失败
	 * 错误码3:你已经取消过该服务（请勿重复取消）
	 * 错误码4:保存到已完成列表失败
	 * 错误码5:删除active表中的订单失败
	 * 错误码6:退钱失败
	 * 错误码7:找不到该订单
	 * 错误码8:你没有权限取消该订单
	 */
	@RequestMapping(value="/cancelOrder",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public String cancelOrder(@RequestParam String orderId,@RequestParam String userId) {
		JSONObject result = new JSONObject();
		ActiveOrderEntity activeOrder = activeOrderRepository.findOne(orderId);
		if (activeOrder == null) {
			return result.put("result",7).toString();
		}
		short orderState = activeOrder.getState();
		short cancelState = activeOrder.getCancelState();
		short orderServeType = activeOrder.getServeType();
		String orderPublisherId = activeOrder.getServePublisherId();
		
		if (orderState == ActiveOrderEntity.STATE_WAIT_AGREE_CREATE) {
			if (!userId.equals(orderPublisherId)&&!userId.equals(activeOrder.getServeReceiverId())) {
				result.put("result",8);
				return result.toString();
			}
			JSONObject cancelResult = reallyCancelOrder(activeOrder, userId,result);
			
			if (cancelResult.getIntValue("result") == 0 && orderServeType == ActiveOrderEntity.NEED_SERVE&&userId.equals(activeOrder.getServeReceiverId())) {
				try {
					NeedServeEntity serve = needServeRepository.getOne(activeOrder.getServeId());
					serve.setState(NeedServeEntity.START_SERVE);
					needServeRepository.save(serve);
				} catch (Exception e) {
					e.printStackTrace();
					result.put("result",2);
					return result.toString();
				}
			}
			return cancelResult.toJSONString();
		}
		
		if (cancelState == ActiveOrderEntity.CANCEL_STATE_NOT_CANCEL) {
			if (userId.equals(orderPublisherId)) {
				activeOrder.setCancelState(ActiveOrderEntity.CANCEL_STATE_PUBLISHER_HAS_CANCEL);
			}else if (userId.equals(activeOrder.getServeReceiverId())) {
				activeOrder.setCancelState(ActiveOrderEntity.CANCEL_STATE_RECEIVER_HAS_CANCEL);
			}else {
				result.put("result",8);
				return result.toString();
			}
			try {
				result.put("result", 0);
				result.put("activeOrderEntity", activeOrder);
				activeOrderRepository.saveAndFlush(activeOrder);
				return result.toString();
			} catch (Exception e) {
				e.printStackTrace();
				result.put("result",1);
				return result.toString();
			}
		}else if (cancelState == ActiveOrderEntity.CANCEL_STATE_PUBLISHER_HAS_CANCEL) {
			if (userId.equals(orderPublisherId)) {
				result.put("result",3);
				return result.toString();
			}else if (userId.equals(activeOrder.getServeReceiverId())) {
				return reallyCancelOrder(activeOrder, userId,result).toJSONString();
			}else {
				result.put("result",8);
				return result.toString();
			}
		}else if (cancelState == ActiveOrderEntity.CANCEL_STATE_RECEIVER_HAS_CANCEL) {
			if (userId.equals(activeOrder.getServeReceiverId())) {
				result.put("result",3);
				return result.toString();
			}else if (userId.equals(activeOrder.getServePublisherId())) {
				return reallyCancelOrder(activeOrder, userId,result).toJSONString();
			}else {
				result.put("result",8);
				return result.toString();
			}
		}
		result.put("result", 9);
		return result.toJSONString();
	}
	
	public JSONObject reallyCancelOrder(ActiveOrderEntity activeOrder,String userId,JSONObject result) {
		FinishedOrderEntity finishedOrder = createFinisherOrder(activeOrder);
		finishedOrder.setState(FinishedOrderEntity.STATE_HAVE_CANCELED);
		try {
			finishedOrderRepository.saveAndFlush(finishedOrder);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", 4);
			return result;
		}
		try {
			activeOrderRepository.delete(activeOrder);
		} catch (Exception e) {
			e.printStackTrace();
			finishedOrderRepository.delete(finishedOrder);
			result.put("result", 5);
			return result;
		}
		if (activeOrder.getServeType() == ActiveOrderEntity.OFFER_SERVE && userId.equals(activeOrder.getServeReceiverId()) ) {
			UserEntity user = userRepository.findOne(activeOrder.getServeReceiverId());
			user.setMoney(activeOrder.getPrice()+user.getMoney());
			try {
				userRepository.save(user);
			} catch (Exception e) {
				activeOrderRepository.saveAndFlush(activeOrder);
				finishedOrderRepository.delete(finishedOrder);
				result.put("result", 6);
				return result;
			}
		}
		result.put("finishedOrderEntity", finishedOrder);
		result.put("result", 0);
		return result;
	}
	
	//注:该方法除结束状态外均已设置上
	public FinishedOrderEntity createFinisherOrder(ActiveOrderEntity activeOrder) {
		FinishedOrderEntity finishedOrderEntity = new FinishedOrderEntity();
		finishedOrderEntity.setBuildDate(activeOrder.getBuildDate());
		finishedOrderEntity.setContent(activeOrder.getContent());
		finishedOrderEntity.setFinishDate(new Date());
		finishedOrderEntity.setOrderId(activeOrder.getOrderId());
		finishedOrderEntity.setPhone(activeOrder.getPhone());
		finishedOrderEntity.setPlace(activeOrder.getPlace());
		finishedOrderEntity.setPrice(activeOrder.getPrice());
		finishedOrderEntity.setRefundResult(activeOrder.getRefundResult());
		finishedOrderEntity.setServeId(activeOrder.getServeId());
		finishedOrderEntity.setServePublisherId(activeOrder.getServePublisherId());
		finishedOrderEntity.setServeReceiverId(activeOrder.getServeReceiverId());
		finishedOrderEntity.setServeType(activeOrder.getServeType());
		finishedOrderEntity.setStartDate(activeOrder.getStartDate());
		finishedOrderEntity.setTitle(activeOrder.getTitle());
		finishedOrderEntity.setType(activeOrder.getType());
		return finishedOrderEntity;
	}
	
	
	@RequestMapping(value="/changeOrderState/{orderId}/{state}", method=RequestMethod.GET)
	public short changeOrderState (@PathVariable("orderId") String orderId,@PathVariable("state") short state) {
		ActiveOrderEntity activeOrderEntity = activeOrderRepository.findOne(orderId);
		if(state == 6) {
			FinishedOrderEntity finishedOrderEntity = new FinishedOrderEntity();
			finishedOrderEntity.setBuildDate(activeOrderEntity.getBuildDate());
			finishedOrderEntity.setContent(activeOrderEntity.getContent());
			finishedOrderEntity.setFinishDate(activeOrderEntity.getFinishDate());//需要讨论
			finishedOrderEntity.setOrderId(activeOrderEntity.getOrderId());
			finishedOrderEntity.setPhone(activeOrderEntity.getPhone());
			finishedOrderEntity.setPlace(activeOrderEntity.getPlace());
			activeOrderRepository.delete(activeOrderEntity);
//			finishedOrderRepository.save(activeOrderEntity);
		}
		activeOrderEntity.setState(state);
		activeOrderRepository.save(activeOrderEntity);
		return 1;
	}

}
