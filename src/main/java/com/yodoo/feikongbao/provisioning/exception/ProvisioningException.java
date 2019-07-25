package com.yodoo.feikongbao.provisioning.exception;

/**
 * @Author houzhen
 * @Date 17:32 2019/5/14
 **/
public class ProvisioningException extends RuntimeException {
    private String messageBundleKey;

    private String[] messageParams;

    public ProvisioningException() {
        super();
    }

    public ProvisioningException(String messageBundleKey, String... messageParams) {
        super(messageBundleKey);
        this.messageParams = messageParams;
        this.messageBundleKey = messageBundleKey;
    }

    public ProvisioningException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getMessageBundleKey() {
        return messageBundleKey;
    }

    public void setMessageBundleKey(String messageBundleKey) {
        this.messageBundleKey = messageBundleKey;
    }

    public String[] getMessageParams() {
        return messageParams;
    }

    public void setMessageParams(String[] messageParams) {
        this.messageParams = messageParams;
    }
}
