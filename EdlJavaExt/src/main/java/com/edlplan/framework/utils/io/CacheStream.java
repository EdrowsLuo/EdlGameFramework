package com.edlplan.framework.utils.io;

public interface CacheStream<I, O> {

    I openInput();

    /**
     *
     * @param append 是否是在流的末尾添加
     * @return
     */
    O openOutput(boolean append);

}
