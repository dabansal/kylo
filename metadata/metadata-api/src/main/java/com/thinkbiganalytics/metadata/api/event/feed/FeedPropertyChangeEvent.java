package com.thinkbiganalytics.metadata.api.event.feed;

import com.thinkbiganalytics.datalake.authorization.model.HadoopAuthorizationGroup;
import com.thinkbiganalytics.metadata.api.event.AbstractMetadataEvent;
import com.thinkbiganalytics.metadata.api.feed.Feed;
import com.thinkbiganalytics.metadata.api.security.HadoopSecurityGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class FeedPropertyChangeEvent extends AbstractMetadataEvent<String> {
    private static final String NIFI_NAMESPACE = "nifi";

    private Properties oldProperties;
    private Properties newProperties;
    private String feedCategory;
    private String feedName;
    private String feedId;
    private List<String> hadoopSecurityGroupNames;

    public FeedPropertyChangeEvent(String feedId, String feedCategory, String feedName, List<String> hadoopSecurityGroupNames, Map<String, Object> oldProperties, Map<String, Object> newProperties) {
        super("");
        this.feedId = feedId;
        this.oldProperties = convertMapToProperties(oldProperties);
        this.newProperties = convertMapToProperties(newProperties);
        this.feedCategory = feedCategory;
        this.feedName = feedName;
        this.hadoopSecurityGroupNames = hadoopSecurityGroupNames;
    }

    public Properties getNifiPropertiesToDelete() {
        Properties nifiProperties = new Properties();
        Properties oldProperties = getOldNifiMetadataProperties();
        Properties newProperties = getNewNifiMetadataProperties();

        oldProperties.forEach((k,v) -> {
            if(newProperties.get(k) == null) {
                nifiProperties.setProperty((String)k,(String)v);
            }
        });
        return nifiProperties;
    }

    private Properties getOldNifiMetadataProperties() {
        Properties nifiProperties = new Properties();
        oldProperties.forEach((k,v) -> {
            String key = (String)k;
            if(key.startsWith(NIFI_NAMESPACE + ":")) {
                nifiProperties.setProperty((String)k, (String)v);
            }
        });

        return nifiProperties;
    }

    private Properties getNewNifiMetadataProperties() {
        Properties nifiProperties = new Properties();
        newProperties.forEach((k,v) -> {
            String key = (String)k;
            if(key.startsWith(NIFI_NAMESPACE + ":")) {
                nifiProperties.setProperty((String)k, (String)v);
            }
        });

        return nifiProperties;
    }

    /*private String getUserDefinedNamespace(String property) {
        String withoutSystemNamespace = property.substring(0, property.indexOf(":") + 1);
        String userNamespace = withoutSystemNamespace.substring(0, withoutSystemNamespace.indexOf(":") - 1);

        return userNamespace;

    }*/

    public Properties getOldProperties() {
        return oldProperties;
    }

    public Properties getNewProperties() {
        return newProperties;
    }

    private Properties convertMapToProperties(Map<String, Object> map) {
        Properties properties = new Properties();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            properties.setProperty(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return properties;
    }

    public String getFeedCategory() {
        return feedCategory;
    }

    public String getFeedName() {
        return feedName;
    }

    public List<String> getHadoopSecurityGroupNames() {
        return hadoopSecurityGroupNames;
    }

    public String getFeedId() {
        return feedId;
    }
}
