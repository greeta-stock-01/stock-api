package net.greeta.stock.product;

import net.greeta.stock.product.command.interceptors.CreateProductCommandInterceptor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import net.greeta.stock.product.core.errorhandling.ProductsServiceEventsErrorHandler;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
	}
	
	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext context, 
			CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
		
	}
	
	@Autowired
	public void configure(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler("product-group", 
				conf -> new ProductsServiceEventsErrorHandler());
		
//		config.registerListenerInvocationErrorHandler("product-group", 
//				conf -> PropagatingErrorHandler.instance());
	}
	
	@Bean(name="productSnapshotTriggerDefinition")
	public SnapshotTriggerDefinition productSnapshotTriggerDefinition(Snapshotter snapshotter) {
		return new EventCountSnapshotTriggerDefinition(snapshotter, 3);
	}

}
