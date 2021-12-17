package com.yeet.yeetonline

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.swagger2.annotations.EnableSwagger2
import javax.sql.DataSource

@EnableJpaRepositories
@SpringBootApplication
class YeetOnlineApplication {
	@Bean
	fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
		val ret = LocalContainerEntityManagerFactoryBean()
		ret.setPackagesToScan("com.yeet.yeetonline")
		ret.dataSource = dataSource
		ret.jpaVendorAdapter = HibernateJpaVendorAdapter()
		return ret
	}
}

@Component
class WebConfig : WebSecurityConfigurerAdapter() {
	override fun configure(http: HttpSecurity) {
		http.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			.oauth2Login()
			.defaultSuccessUrl("/login_success")
			.failureUrl("/login_failure")
	}
}

fun main(args: Array<String>) {
	runApplication<YeetOnlineApplication>(*args)
}
