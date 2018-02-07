package net.$51zhiyuan.development.kidbridge.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 缺省监听器实现
 */
@Component
public class DefaultListener implements ApplicationListener<ContextRefreshedEvent>, Runnable {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {

        }
    }

    @Override
    public void run() {

    }
}
