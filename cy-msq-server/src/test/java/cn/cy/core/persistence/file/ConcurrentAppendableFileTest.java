package cn.cy.core.persistence.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ConcurrentAppendableFileTest {

    private Path path;

    private volatile ConcurrentAppendableFile concurrentAppendableFile;

    @Before
    public void init() throws FileNotFoundException {
        path = Paths.get("message_file_1.msg");
        concurrentAppendableFile = new ConcurrentAppendableFile(path, false);
    }

    @After
    public void after() throws IOException {
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    public String readFromFile() throws IOException {
        List<String> stringList = Files.readAllLines(path);

        StringBuilder sb = new StringBuilder();

        for (String s : stringList) {
            sb.append(s);
        }

        // 由于是预分配空间的, 所以trim之后才是真正内容
        return sb.toString().trim();
    }

    /**
     * 测试单线程追加文件内容
     */
    @Test
    public void testSingleThread() throws URISyntaxException, IOException {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 100; i++) {
            String x = Strings.repeat("zxcvasdfqwer", Math.abs(new Random().nextInt()) % 10000 + 1);
            concurrentAppendableFile.append(x);
            sb.append(x);
        }

        String res = readFromFile();

        Assert.assertEquals(res, sb.toString());
    }

    /**
     * 测试多线程追加
     */
    @Test
    public void testMultiThreadAppend() throws IOException, ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(20);

        Object object = new Object();
        String pat = "qwer";
        List<Future<String>> futures = Lists.newArrayList();
        int taskCnt = 1000;

        for (int i = 0; i < taskCnt; i++) {
            Future<String> future = executor.submit(new Appender(concurrentAppendableFile, object, pat));

            futures.add(future);
        }

        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();

        for (Future<String> future : futures) {
            String content = future.get();
            map2.compute(content, (key, oldValue) -> {
                if (oldValue == null) {
                    return 1;
                } else {
                    return oldValue + 1;
                }
            });
        }

        Files.lines(path).forEach(line -> map1.compute(line + "\n", (key, oldValue) -> {

            // 因为有空白部分填充,所以填充部分忽略掉,以免影响比较
            if (!key.startsWith(pat)) {
                return null;
            }

            if (oldValue == null) {
                return 1;
            } else {
                return oldValue + 1;
            }
        }));

        Assert.assertTrue(Maps.difference(map1, map2).areEqual());
        Assert.assertEquals(map1.values().stream().mapToInt(x -> x).sum(), taskCnt);
    }

    private static class Appender implements Callable<String> {

        private ConcurrentAppendableFile concurrentAppendableFile;

        private final Object sync;

        private final String pattern;

        public Appender(ConcurrentAppendableFile concurrentAppendableFile, Object sync, String pattern) {
            this.concurrentAppendableFile = concurrentAppendableFile;
            this.sync = sync;
            this.pattern = pattern;
        }

        @Override
        public String call() {
            String x = Strings.repeat(pattern, Math.abs(new Random().nextInt()) % 10000 + 1) + "\n";
            try {
                concurrentAppendableFile.append(x);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return x;
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFoundThrowException() throws FileNotFoundException {
        path = Paths.get("message_file_1_xxx.msg");
        concurrentAppendableFile = new ConcurrentAppendableFile(path, true);
    }
}