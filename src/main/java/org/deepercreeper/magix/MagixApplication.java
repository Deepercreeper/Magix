package org.deepercreeper.magix;

import org.deepercreeper.engine.config.EngineConfiguration;
import org.deepercreeper.engine.physics.Engine;
import org.deepercreeper.magix.config.WindowsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({WindowsConfiguration.class, EngineConfiguration.class})
public class MagixApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MagixApplication.class);
        springApplication.setHeadless(false);
        ApplicationContext applicationContext = springApplication.run(args);
        Engine engine = applicationContext.getBean(Engine.class);
        engine.start();
        engine.await();
        System.exit(SpringApplication.exit(applicationContext));
    }
}
