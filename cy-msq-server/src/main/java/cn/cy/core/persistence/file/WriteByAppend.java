package cn.cy.core.persistence.file;

import java.io.IOException;

public interface WriteByAppend {
    AppendInfo append(CharSequence seq) throws IOException;
}
