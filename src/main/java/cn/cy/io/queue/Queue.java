package cn.cy.io.queue;

public interface Queue<T> {

    boolean push(T msg);

    T pull();

}
