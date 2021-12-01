package com.yeet.yeetonline

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
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

fun main(args: Array<String>) {
	runApplication<YeetOnlineApplication>(*args)
}
