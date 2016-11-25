package com.anjz.core.controller.monitor;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anjz.base.controller.BaseController;
import com.anjz.result.MapResult;
import com.anjz.util.PrettyMemoryUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 * ehcache监控
 * 
 * @author ding.shuai
 * @date 2016年9月11日上午8:57:47
 */
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("/monitor/ehcache")
@RequiresPermissions("monitor:ehcache:*")
public class EhcacheMonitorController extends BaseController  implements InitializingBean{

	@Resource
	private CacheManager cacheManager;
	
	private net.sf.ehcache.CacheManager cachemanager;

	@RequestMapping(value = "")
	public String index(Model model) {	
		model.addAttribute("cacheManager", cachemanager);
		return viewName("index");
	}

	@RequestMapping("{cacheName}/details")
	public String details(@PathVariable("cacheName") String cacheName,
			@RequestParam(value = "searchText", required = false, defaultValue = "") String searchText, Model model) {

		model.addAttribute("cacheName", cacheName);
		List allKeys = cachemanager.getCache(cacheName).getKeys();

		List<Object> showKeys = Lists.newArrayList();

		for (Object key : allKeys) {
			if (key.toString().contains(searchText)) {
				showKeys.add(key);
			}
		}

		model.addAttribute("keys", showKeys);

		return viewName("details");
	}
	
	
	@RequestMapping("{cacheName}/{key}/details")
    @ResponseBody
    public MapResult<String, Object> keyDetail(@PathVariable("cacheName") String cacheName,
            @PathVariable("key") String key, Model model) {		
		MapResult<String, Object> result=new MapResult<String, Object>();
		Cache cache= cachemanager.getCache(cacheName);
		List allKeys = cache.getKeys();
		
		Element element=null;
		//key class org.apache.shiro.subject.SimplePrincipalCollection  授权缓存的key类型
		for(Object k:allKeys){
			if(k.toString().equals(key)){
				element=cache.get(k);
				break;
			}
		}
			
		if(element==null){
			result.setErrorMessage("缓存["+key+"]已经不存在,请手动刷新当前页面");
			return result;
		}
		
        String dataPattern = "yyyy-MM-dd hh:mm:ss";
        Map<String, Object> data = Maps.newHashMap();
        data.put("objectValue", element.getObjectValue().toString());
        data.put("size", PrettyMemoryUtils.prettyByteSize(element.getSerializedSize()));
        data.put("hitCount", element.getHitCount());

        Date latestOfCreationAndUpdateTime = new Date(element.getLatestOfCreationAndUpdateTime());
        data.put("latestOfCreationAndUpdateTime", DateFormatUtils.format(latestOfCreationAndUpdateTime, dataPattern));
        Date lastAccessTime = new Date(element.getLastAccessTime());
        data.put("lastAccessTime", DateFormatUtils.format(lastAccessTime, dataPattern));
        if(element.getExpirationTime() == Long.MAX_VALUE) {
            data.put("expirationTime", "不过期");
        } else {
            Date expirationTime = new Date(element.getExpirationTime());
            data.put("expirationTime", DateFormatUtils.format(expirationTime, dataPattern));
        }

        data.put("timeToIdle", element.getTimeToIdle());
        data.put("timeToLive", element.getTimeToLive());
        data.put("version", element.getVersion());

        result.setData(data);
        return result;

    }
	
	@RequestMapping("{cacheName}/{key}/delete")
    @ResponseBody
    public Object delete( @PathVariable("cacheName") String cacheName,@PathVariable("key") String key) {
        Cache cache = cachemanager.getCache(cacheName);
        List allKeys = cache.getKeys();
        for(Object k:allKeys){
			if(k.toString().equals(key)){
				cache.remove(k);
				break;
			}
		}
        return "操作成功！";
    }
	
	@RequestMapping("{cacheName}/clear")
    @ResponseBody
    public Object clear(@PathVariable("cacheName") String cacheName) {
        Cache cache = cachemanager.getCache(cacheName);
        cache.clearStatistics();
        cache.removeAll();
        return "操作成功！";
    }
	

	@Override
	public void afterPropertiesSet() throws Exception {		
		//如果底层用的是EHCACHE，进行强转
		if(cacheManager instanceof EhCacheCacheManager){
			EhCacheCacheManager ehCacheCacheManager=(EhCacheCacheManager)cacheManager;
			cachemanager=ehCacheCacheManager.getCacheManager();
		}	
	}
}
