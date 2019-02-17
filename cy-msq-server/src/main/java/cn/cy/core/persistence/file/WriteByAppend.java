package cn.cy.core.persistence.file;

import java.io.IOException;

public interface WriteByAppend {
    void append(CharSequence seq) throws IOException;
}
