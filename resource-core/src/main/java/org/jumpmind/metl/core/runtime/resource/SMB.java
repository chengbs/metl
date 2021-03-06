package org.jumpmind.metl.core.runtime.resource;

import org.jumpmind.properties.TypedProperties;

public class SMB extends AbstractResourceRuntime {

    public static final String TYPE = "SMB";

    public static final String SMB_BASE_URL = "smb.base.url";

    // User is not required since it can be left blank
    public static final String SMB_USER = "smb.user";

    // Password is not required since it can be blank.
    public static final String SMB_PASSWORD = "smb.password";

    // Domain is not required since it can be blank.
    public final static String SMB_DOMAIN = "smb.domain";

    IDirectory streamableResource;

    @Override
    protected void start(TypedProperties properties) {
        streamableResource = new SMBDirectory(properties.getProperty(SMB_BASE_URL), properties.getProperty(SMB_USER),
                properties.getProperty(SMB_PASSWORD), properties.getProperty(SMB_DOMAIN));
    }

    @Override
    public void stop() {
        streamableResource.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T reference() {
        return (T) streamableResource;
    }

}
