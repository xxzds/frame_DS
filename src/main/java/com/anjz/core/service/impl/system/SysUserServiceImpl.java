package com.anjz.core.service.impl.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.pageandsort.Page;
import com.anjz.base.entity.search.pageandsort.PageImpl;
import com.anjz.base.service.BaseServiceImpl;
import com.anjz.core.dao.SysUserDao;
import com.anjz.core.enums.UserStatus;
import com.anjz.core.model.SysUser;
import com.anjz.core.model.SysUserOrganizationJob;
import com.anjz.core.model.SysUserStatusHistory;
import com.anjz.core.service.intf.system.PasswordHelperService;
import com.anjz.core.service.intf.system.SysUserOrganizationJobService;
import com.anjz.core.service.intf.system.SysUserService;
import com.anjz.core.service.intf.system.SysUserStatusHistoryService;
import com.anjz.exception.BusinessException;
import com.anjz.result.BaseResult;
import com.anjz.result.PlainResult;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author ding.shuai
 * @date 2016年9月3日上午10:31:12
 */
@Service
public class SysUserServiceImpl  extends BaseServiceImpl<SysUser, String> implements SysUserService {

	@Resource
	private SysUserDao sysUserDao;

	@Resource
	private SysUserOrganizationJobService sysUserOrganizationJobService;

	@Resource
	private PasswordHelperService passwordHelperService;
	
	@Resource
	private SysUserStatusHistoryService sysUserStatusHistoryService;

	
	/**
	 * 通过用户名查询实体
	 * @param userName
	 * @return
	 */
	@Override
	@Cacheable(value="currentUserCache",key="#userName")
	public PlainResult<SysUser> findByUsername(String userName) {
		PlainResult<SysUser> result = new PlainResult<SysUser>();

		SysUser user = new SysUser();
		user.setUserName(userName);
		List<SysUser> list = sysUserDao.find(user);

		if (!list.isEmpty()) {
			result.setData(list.get(0));
		} else {
			result.setErrorMessage("查询数据为空");
		}
		return result;
	}
	
	
	/**
	 * 通过邮箱查询实体
	 * @param userEmail
	 * @return
	 */
	@Override
	public PlainResult<SysUser> findByUserEmail(String userEmail){
		PlainResult<SysUser> result = new PlainResult<SysUser>();

		SysUser user = new SysUser();
		user.setUserEmail(userEmail);
		List<SysUser> list = sysUserDao.find(user);

		if (!list.isEmpty()) {
			result.setData(list.get(0));
		} else {
			result.setErrorMessage("查询数据为空");
		}
		return result;
	}
	
	
	/**
	 * 通过手机号查询实体
	 * @param userPhone
	 * @return
	 */
	@Override
	public PlainResult<SysUser> findByUserPhone(String userPhone){
		PlainResult<SysUser> result = new PlainResult<SysUser>();

		SysUser user = new SysUser();
		user.setUserPhone(userPhone);
		List<SysUser> list = sysUserDao.find(user);

		if (!list.isEmpty()) {
			result.setData(list.get(0));
		} else {
			result.setErrorMessage("查询数据为空");
		}
		return result;
	}
	
	/**
	 * 通过条件查询实体，并分页
	 * @param organizationId 组织id
	 * @param jobId  职务id
	 * @param searchable
	 * @return
	 */
    public Page<SysUser> findSysUserWithPage(String organizationId,String jobId,Searchable searchable){
    	if("0".equals(organizationId) && "0".equals(jobId)){
    		return this.findAll(searchable);
    	}
    	
    	SysUserOrganizationJob sysUserOrganizationJob=new SysUserOrganizationJob();
    	if(StringUtils.isNotEmpty(organizationId) && !"0".equals(organizationId)){
    		sysUserOrganizationJob.setOrganizationId(organizationId);
    	}
    	if(StringUtils.isNotEmpty(jobId) && !"0".equals(jobId)){
    		sysUserOrganizationJob.setJobId(jobId);
    	}
    	
    	List<SysUserOrganizationJob> lists=sysUserOrganizationJobService.find(sysUserOrganizationJob).getData();
    	
		Set<String> userIds = Sets.newHashSet(
				Collections2.transform(lists, new Function<SysUserOrganizationJob, String>() {
					@Override
					public String apply(SysUserOrganizationJob input) {
						return input.getUserId();
					}
				}));
		
		if(userIds.isEmpty()){
			return new PageImpl<SysUser>(new ArrayList<SysUser>());
		}
		
		searchable.addSearchFilter("id", SearchOperator.in, userIds);
		return this.findAll(searchable);
    }
	
	
	/**
	 * 添加用户数据，以及用户的组织机构和职位
	 * @param user
	 * @param organizationJobs
	 * @return
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public BaseResult saveUserAndOrganazionAndJob(SysUser user,List<SysUserOrganizationJob> organizationJobs){
		BaseResult result=new BaseResult();
		if(user==null){
			result.setErrorMessage("实体[user]为空");
			return result;
		}
		
		passwordHelperService.encryptPassword(user);
		BaseResult saveUserResult= this.saveSelective(user);
		if(!saveUserResult.isSuccess()){
			throw new BusinessException("添加用户失败");
		}
				
		if(!organizationJobs.isEmpty()){
			String userId=user.getId();
			for(SysUserOrganizationJob sysUserOrganizationJob:organizationJobs){
				sysUserOrganizationJob.setUserId(userId);
				BaseResult saveUserOrganizationJobResult=sysUserOrganizationJobService.saveSelective(sysUserOrganizationJob);
				if(!saveUserOrganizationJobResult.isSuccess()){
					throw new BusinessException("添加用户的部门和职位失败");
				}
			}
		}
		return result;
	}
	
	/**
	 * 更新用户数据，以及用户的组织机构和职位
	 * @param user
	 * @param organizationJobs
	 * @return
	 */
	@CacheEvict(value="currentUserCache",key="#user.userName")
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public BaseResult updateUserAndOrganazionAndJob(SysUser user,List<SysUserOrganizationJob> organizationJobs){
		BaseResult result=new BaseResult();
		if(user==null){
			result.setErrorMessage("实体[user]为空");
			return result;
		}
		
		//1.更新用户表
		BaseResult saveUserResult= this.updateSelectiveById(user);
		if(!saveUserResult.isSuccess()){
			throw new BusinessException("添加用户失败");
		}
		
		
		//2.删除组织机构，职位关联表
		SysUserOrganizationJob model=new SysUserOrganizationJob();
		model.setUserId(user.getId());
		List<SysUserOrganizationJob> userOrganizationJobList= sysUserOrganizationJobService.find(model).getData();
		
		List<String> userOrganizationJobIds=Lists.newArrayList(
				Collections2.transform(userOrganizationJobList, new Function<SysUserOrganizationJob, String>(){
					@Override
					public String apply(SysUserOrganizationJob input) {						
						return input.getId();
					}
				}));		
		if(!userOrganizationJobIds.isEmpty()){
			sysUserOrganizationJobService.batchDeleteByIds(userOrganizationJobIds.toArray(new String[]{}));
		}
		
		//3.添加组织机构，职位关联表	
		if(!organizationJobs.isEmpty()){
			String userId=user.getId();
			for(SysUserOrganizationJob sysUserOrganizationJob:organizationJobs){
				sysUserOrganizationJob.setUserId(userId);
				BaseResult saveUserOrganizationJobResult=sysUserOrganizationJobService.saveSelective(sysUserOrganizationJob);
				if(!saveUserOrganizationJobResult.isSuccess()){
					throw new BusinessException("添加用户的部门和职位失败");
				}
			}
		}
		return result;
	}
	
	
	/**
	 * 删除用户
	 * @param userId
	 * @return
	 */
	@CacheEvict(value="currentUserCache",allEntries=true)
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public BaseResult deleteUser(String userId){
		BaseResult result=new BaseResult();
		
		//1.删除user表中的数据
		BaseResult delelteResult= this.deleteById(userId);
		if(!delelteResult.isSuccess()){
			throw new BusinessException("删除用户失败");
		}
		
		//2.删除用户关联的机构表和职务表
		SysUserOrganizationJob model=new SysUserOrganizationJob();
		model.setUserId(userId);
		List<SysUserOrganizationJob> userOrganizationJobList= sysUserOrganizationJobService.find(model).getData();
		
		List<String> userOrganizationJobIds=Lists.newArrayList(
				Collections2.transform(userOrganizationJobList, new Function<SysUserOrganizationJob, String>(){
					@Override
					public String apply(SysUserOrganizationJob input) {						
						return input.getId();
					}
				}));		
		if(!userOrganizationJobIds.isEmpty()){
			sysUserOrganizationJobService.batchDeleteByIds(userOrganizationJobIds.toArray(new String[]{}));
		}
		
		return result;
	}
	
	/**
	 * 批量更新用户数据
	 * @param users
	 * @return
	 */
	@CacheEvict(value="currentUserCache",allEntries=true)
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public BaseResult batchUpdate(List<SysUser> users){		
		for(SysUser user:users){
			BaseResult baseResult= this.updateSelectiveById(user);
			if(!baseResult.isSuccess()){
				throw new BusinessException("用户批量更新失败");
			}
		}
		
		return new BaseResult();
	}
	
	/**
	 * 修改用户密码
	 * @param userIds
	 * @param newPassword
	 * @return
	 */
	@CacheEvict(value="currentUserCache",allEntries=true)
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public BaseResult changePassword(String[] userIds,String newPassword){
		BaseResult result=new BaseResult();		
		if(userIds==null || userIds.length==0){
			return result;
		}
		
		for(String userId:userIds){
			SysUser user= this.findOne(userId).getData();
			if(user==null){
				result.setErrorMessage("用户["+userId+"]不存在，修改密码无法完成");
				return result;
			}
			
			user.setUserPassword(newPassword);
			passwordHelperService.encryptPassword(user);
			BaseResult saveUserResult= this.updateSelectiveById(user);
			if(!saveUserResult.isSuccess()){
				throw new BusinessException("修改用户["+userId+"]的密码失败");
			}
		}
		return result;
	}
	
	/**
	 * 更改用户状态
	 * @param opUser  操作人
	 * @param userIds  被操作的用户ID
	 * @param newStatus 新状态
	 * @param reson 操作原因
	 * @return
	 */
	@CacheEvict(value="currentUserCache",allEntries=true)
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public BaseResult changeStatus(SysUser opUser, String[] userIds,UserStatus newStatus,String reason){

		for(String userId:userIds){
			//1.更新用户状态
			SysUser user=new SysUser();
			user.setId(userId);
			user.setStatus(newStatus);
			BaseResult updateResult= this.updateSelectiveById(user);
			if(!updateResult.isSuccess()){
				throw new BusinessException("更新用户状态失败");
			}
			
			//2.添加更新用户状态的记录
			SysUserStatusHistory sysUserStatusHistory=new SysUserStatusHistory();
			sysUserStatusHistory.setUserId(userId);
			sysUserStatusHistory.setStatus(newStatus);
			sysUserStatusHistory.setOpUserId(opUser.getId());
			sysUserStatusHistory.setReason(reason);
			sysUserStatusHistory.setOpDate(new Date());
			BaseResult saveResult= sysUserStatusHistoryService.saveSelective(sysUserStatusHistory);
			if(!saveResult.isSuccess()){
				throw new BusinessException("添加用户状态记录失败");
			}
		}
		return new BaseResult();
	}

	
	/**
	 * 通过用户名查询用户名和id存入map
	 * [{"label":用户名,"value":用户id},{}]
	 * @param searchable
	 * @param usernme
	 * @return
	 */
	public Set<Map<String, Object>> findIdAndNames(Searchable searchable, String usernme){
		
		searchable.addSearchFilter("user_name", SearchOperator.like, usernme);
        searchable.addSearchFilter("deleted", SearchOperator.eq, false);

        return Sets.newHashSet(
                Lists.transform(
                        findAll(searchable).getContent(),
                        new Function<SysUser, Map<String, Object>>() {
                            @Override
                            public Map<String, Object> apply(SysUser input) {
                                Map<String, Object> data = Maps.newHashMap();
                                data.put("label", input.getUserName());
                                data.put("value", input.getId());
                                return data;
                            }
                        }
                )
        );
	}
	
	@Override
	@CacheEvict(value="currentUserCache",key="#m.userName")
	public BaseResult updateAllById(SysUser m) {
		return super.updateAllById(m);
	}
	
	@Override
	@CacheEvict(value="currentUserCache",key="#m.userName")
	public BaseResult updateSelectiveById(SysUser m) {
		return super.updateSelectiveById(m);
	}
	
	@Override
	@CacheEvict(value="currentUserCache",allEntries=true)
	public BaseResult deleteById(String id) {
		return super.deleteById(id);
	}
}
