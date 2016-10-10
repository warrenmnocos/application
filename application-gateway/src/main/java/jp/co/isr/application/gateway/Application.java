/*
 * Copyright 2016 International Systems Research Co. (ISR).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.co.isr.application.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * This is the starter for this microservice. Extending
 * {@link SpringBootServletInitializer} will make this app deployable in any
 * Servlet-based web servers like Tomcat. If intended to make this app
 * executable only, extending {@link SpringBootServletInitializer} can be
 * avoided.
 *
 * @author Warren Nocos
 * @since 1.0
 * @version 1.0
 */
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class Application extends SpringBootServletInitializer {

    /**
     * {@inheritDoc }
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    /**
     * This is where the application is bootstrapped if deployed without an
     * external server.
     *
     * @param arguments the command line arguments
     */
    public static void main(String... arguments) {
        SpringApplication.run(Application.class, arguments);
    }

}
