package cn.weegoo.quartz.task;

import cn.weegoo.quartz.domain.Task;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class TestTask1 extends Task {

    @Override
    public void run() {
        System.out.println ( "这是另一个测试任务TestTask1。" );

    }

}
