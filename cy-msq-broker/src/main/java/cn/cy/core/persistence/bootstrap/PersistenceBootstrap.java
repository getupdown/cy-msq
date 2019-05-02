package cn.cy.core.persistence.bootstrap;

/**
 * 持久化引导启动器
 */
public class PersistenceBootstrap {

    //    private static PersistenceManager persistenceManager;
    //
    //    private static QueueConfiguration queueConfiguration = new QueueConfiguration();
    //
    //    public static void main(String[] args) throws FileNotFoundByIndexException, IOException, CheckSumException {
    //
    //        PersistentWriteDispatcher writeDispatcher = new MessageFileWriteDispatcher(queueConfiguration);
    //
    //        ByteIndexBySeq byteIndexBySeq = new MemoryByteIndexImpl();
    //
    //        IndexReadDispatcher readDispatcher = new IndexReadDispatcherImpl(queueConfiguration);
    //
    //        persistenceManager =
    //                new PersistenceManager(writeDispatcher, byteIndexBySeq, readDispatcher, new LocalQueueStateImpl
    //                ());
    //
    //        for (int i = 0; i < 2000000; i++) {
    //            QueuedMessage queuedMessage = new QueuedMessage();
    //
    //            queuedMessage.setCheckSum("a");
    //
    //            queuedMessage.setContent("qwerasdf");
    //
    //            persistenceManager.write(queuedMessage);
    //        }
    //
    //        persistenceManager.read(1L);
    //    }

}
