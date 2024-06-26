package org.acme.exception;

public class UploadException extends RuntimeException {
    
    public UploadException(){
        super("Error with upload image");
    }
    public UploadException(Throwable cause){
        super("Error with upload image", cause);
    }

}
