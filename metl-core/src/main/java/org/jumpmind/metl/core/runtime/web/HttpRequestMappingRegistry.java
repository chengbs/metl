package org.jumpmind.metl.core.runtime.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jumpmind.metl.core.model.AgentDeploy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

public class HttpRequestMappingRegistry implements IHttpRequestMappingRegistry {

    final Logger log = LoggerFactory.getLogger(getClass());

    AntPathMatcher patternMatcher = new AntPathMatcher();
    
    Map<HttpMethod, Set<HttpRequestMapping>> mappingsByHttpMethod = new HashMap<>();
    
    Map<AgentDeploy, Set<HttpRequestMapping>> mappingsByAgentDeployment = new HashMap<>();
    
    @Override
    public HttpRequestMapping findBestMatch(HttpMethod method, String path) {
        Set<HttpRequestMapping> mappings = mappingsByHttpMethod.get(method);
        if (mappings != null) {
            for (HttpRequestMapping httpRequestMapping : mappings) {
                log.info(String.format("Found available uri: %s",httpRequestMapping.getPath()));
                if (patternMatcher.match(httpRequestMapping.getPath(), path)) {
                    return httpRequestMapping;
                }
            }
        }
        return null;
    }
    
    @Override
    public List<HttpRequestMapping> getHttpRequestMappingsFor(AgentDeploy deployment) {
        Set<HttpRequestMapping> mappings = mappingsByAgentDeployment.get(deployment);
        if (mappings != null) {
            return new ArrayList<>(mappings);
        } else {
            return Collections.emptyList();
        }
    }  
    
    @Override
    public void register(HttpRequestMapping request) {
        Set<HttpRequestMapping> mappings = mappingsByHttpMethod.get(request.getMethod());
        if (mappings == null) {
            mappings = new TreeSet<>();
            mappingsByHttpMethod.put(request.getMethod(), mappings);
        }
        mappings.add(request);        
        
        mappings = mappingsByAgentDeployment.get(request.getDeployment());
        if (mappings == null) {
            mappings = new TreeSet<>();
            mappingsByAgentDeployment.put(request.getDeployment(), mappings);
        }
                
        mappings.add(request);
        
        log.info("Registering REST service: {}", request);
    }
    
    @Override
    public void unregister(HttpRequestMapping request) {
        boolean unregistered = false;
        Set<HttpRequestMapping> mappings = mappingsByHttpMethod.get(request.getMethod());
        if (mappings != null) {            
            unregistered |= mappings.remove(request);
        }
        
        mappings = mappingsByAgentDeployment.get(request.getDeployment());
        if (mappings != null) {
            unregistered |= mappings.remove(request);
        }
        
        if (unregistered) {
            log.info("Unregistering REST service: {}", request);
        }
    }
    
    
}
