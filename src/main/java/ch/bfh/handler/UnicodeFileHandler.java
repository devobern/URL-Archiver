package ch.bfh.handler;

import ch.bfh.validator.PathValidator;
import ch.bfh.exceptions.PathValidationException;
import ch.bfh.validator.UnicodeFileValidator;
import ch.bfh.exceptions.UnicodeFileFormatException;

public class UnicodeFileHandler {
    
    private String path;
    private String fileName;
    private String fileType;

    /**
     * Constructor for UnicodeFileHandler each file has its own handler
     * @param path needs as input a path to a file
     * @throws PathValidationException throws Exception if path is invalid or path isn't a file
     */
    public UnicodeFileHandler(String path) throws PathValidationException, UnicodeFileFormatException {
        
        PathValidator pathValidator = new PathValidator();
        pathValidator.validate(path);

        UnicodeFileValidator unicodeFileValidator = new UnicodeFileValidator();
        unicodeFileValidator.validate(path);

    }

}
