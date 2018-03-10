package bid.xiaocha.xxt_server.rest_controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bid.xiaocha.xxt_server.entities.CollectOfferServeEntity;
import bid.xiaocha.xxt_server.entities.OfferServeEntity;
import bid.xiaocha.xxt_server.model.CollectResult;
import bid.xiaocha.xxt_server.model.GetResultByPage;
import bid.xiaocha.xxt_server.repositories.CollectOfferServeRepository;
import bid.xiaocha.xxt_server.repositories.OfferServeRepository;
import bid.xiaocha.xxt_server.utils.CommonUtils;
import groovyjarjarasm.asm.tree.IntInsnNode;

@RestController
public class CollectController {

	@Autowired
	private CollectOfferServeRepository collectOfferServeRepository;
	@Autowired
	private OfferServeRepository offerServeRepository;
	@Autowired
	private ServeController serveController;
	
	@RequestMapping(value="/getCollectListByPage", method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public GetResultByPage<CollectResult> getCollectListByPage(@RequestParam String userId,@RequestParam int pageNum){
		GetResultByPage<CollectResult> result = new GetResultByPage<CollectResult>();
		Sort sort = new Sort(Direction.DESC,"collectDate");
		Page<CollectOfferServeEntity> page =null;
		Pageable pageable = new PageRequest(pageNum,CommonUtils.NumInAPage,sort);
		
		page = collectOfferServeRepository.findByUserIdByPage(userId, pageable);
		List<CollectOfferServeEntity> list = page.getContent();
		List<String> serveIdList = new ArrayList<>();
		for(CollectOfferServeEntity serveEntity : list) {
			serveIdList.add(serveEntity.getServeId());
		}
		List<OfferServeEntity> offerServeList = serveController.getServeByServeIdList(serveIdList);
		CollectResult collectResult = new CollectResult();
		List<CollectResult> collectResults = new ArrayList<>();
		for(int i=0; i<list.size(); i++) {
			collectResult.setDate(list.get(i).getCollectDate());
			collectResult.setOfferServeEntity(offerServeList.get(i));
			collectResults.add(collectResult);
		}
		
		if(page.hasContent()) {
			result.setHaveMore(true);
			result.setDataList(collectResults);
		}
		else {
			result.setHaveMore(false);
			result.setDataList(null);
		}
		return result;
	}
	
	@RequestMapping(value="/findOneCollect",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public Short findOneCollect(@RequestParam String userId,@RequestParam String serveId) {
		CollectOfferServeEntity collectOfferServeEntity = collectOfferServeRepository.findByUserIdAndServeId(userId, serveId);
		if(collectOfferServeEntity==null) {
			return 1;
		}
		else
			return 0;
	}
	
	//返回码
	//0-成功
	//1-失败
	//2-服务器故障
	//3-重复收藏
	@RequestMapping(value="/addOneCollect",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public short addOneCollect(@RequestParam String userId,@RequestParam String serveId) {
		 CollectOfferServeEntity collectOfferServeEntity = collectOfferServeRepository.findByUserIdAndServeId(userId, serveId);
		 if(collectOfferServeEntity != null) {
			return 3;
		 }else {
			collectOfferServeEntity = new CollectOfferServeEntity();
			collectOfferServeEntity.setServeId(serveId);
			collectOfferServeEntity.setUserId(userId);
			Date date = new Date();
			collectOfferServeEntity.setCollectDate(date);
			try {
				collectOfferServeRepository.saveAndFlush(collectOfferServeEntity);
				return 0;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return 2;
				
			}
			
		}
	}
	
	@RequestMapping(value="/deleteOneCollect",method=RequestMethod.GET)
	@PreAuthorize("principal.equals(#userId)")
	public short deleteOneCollect(@RequestParam String userId,@RequestParam String serveId) {
		 CollectOfferServeEntity collectOfferServeEntity = collectOfferServeRepository.findByUserIdAndServeId(userId, serveId);
		 if(collectOfferServeEntity == null) {
			return 3;
		 }else {
			
			try {
				collectOfferServeRepository.delete(collectOfferServeEntity);
				return 0;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return 2;
				
			}
			
		}
	}
	
}
