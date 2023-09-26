package cn.weegoo.quartz.task;

import cn.weegoo.quartz.domain.Task;
import org.quartz.DisallowConcurrentExecution;

@DisallowConcurrentExecution
public class TestTask extends Task {

    @Override
    public void run() {
        System.out.println ( "这是测试任务TestTask。" );

    }

}
