package cn.cy.core.persistence.file;

/**
 * 支持行随机读取的文件
 */
public interface LineRandomAccess {

    String getSpecificLine(Integer lineNum);
    
}
