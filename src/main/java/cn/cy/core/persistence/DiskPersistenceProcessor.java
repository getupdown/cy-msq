package cn.cy.core.persistence;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 基于硬盘的持久化类
 * <p>
 * 基于mmap操作
 */
public class DiskPersistenceProcessor implements PersistenceProcessor {

    private FileChannel fileChannel;

    private Path filePath;

    public DiskPersistenceProcessor(Path path) {
        filePath = path;
    }

    /**
     * 把消息追加持久化至硬盘上
     *
     * @param target
     *
     * @throws IOException
     */
    @Override
    public void appendPersistent(Byte[] target) throws IOException {
        ensureOpen();

        // 在这里采用了直接mmap追加的方式
    }

    /**
     * 把指定字节装入
     *
     * @param start
     * @param size
     */
    @Override
    public Byte[] read(long start, long size) throws IOException {
        ensureOpen();

        /**
         * 这里会发现, 每次map出来之后, buffer里内容的改变对其他进程是可见的
         * 但是buffer里那些变量的内容是各自维护的, 所以每次map只是一次系统调用
         */
        MappedByteBuffer mbb = fileChannel.map(FileChannel.MapMode.READ_ONLY, start, size);

        // 堆外内存不支持直接#array操作, 所以只能慢慢读取
        List<Byte> res = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            res.add(mbb.get());
        }

        return res.toArray(new Byte[0]);
    }

    @Override
    public FileChannel getFileChannel() throws IOException {
        ensureOpen();
        return fileChannel;
    }

    private void ensureOpen() throws IOException {
        if (fileChannel == null) {
            fileChannel = FileChannel.open(filePath);
        }
    }
}
