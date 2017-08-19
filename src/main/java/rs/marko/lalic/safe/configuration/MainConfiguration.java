package rs.marko.lalic.safe.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import rs.marko.lalic.safe.core.audit.AuditInterceptor;
import rs.marko.lalic.safe.core.exceptions.InternalErrorException;
import rs.marko.lalic.safe.core.processors.file.AddFileProcessor;
import rs.marko.lalic.safe.core.processors.folder.CreateFolderProcessor;
import rs.marko.lalic.safe.core.processors.person.RegisterPersonProcessor;
import rs.marko.lalic.safe.core.security.SecurityInterceptor;
import rs.marko.lalic.safe.core.services.*;
import org.apache.tomcat.jdbc.pool.DataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "rs.marko.lalic.safe")
@PropertySource(value = "classpath:safe.properties")
public class MainConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private AuditInterceptor auditInterceptor;

    @Autowired
    private SecurityInterceptor securityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
	registry.addInterceptor(auditInterceptor);
	registry.addInterceptor(securityInterceptor);
    }

    @Bean(name = "auditInterceptor")
    public AuditInterceptor auditInterceptor() {
	return new AuditInterceptor();
    }

    @Bean(name = "securityInterceptor")
    public SecurityInterceptor securityInterceptor(@Value("${service.admin.token}") String authorizationToken)
		    throws InternalErrorException {
	SecurityInterceptor si = new SecurityInterceptor();
	si.setAuthorizationToken(authorizationToken);
	return si;
    }

    @Bean(name = "propertySourcesPlaceholderConfigurer")
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
	return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createMultipartResolver() {
	CommonsMultipartResolver resolver = new CommonsMultipartResolver();
	resolver.setDefaultEncoding("UTF-8");
	return resolver;
    }

    @Bean(name = "dataSource", destroyMethod = "close")
    public DataSource dataSource(@Value("${database.driver.class.name}") String driverClassName,
		    @Value("${database.url}") String url, @Value("${database.username}") String username,
		    @Value("${database.password}") String password,
		    @Value("${database.pool.min.active}") int initialSize,
		    @Value("${database.pool.max.active}") int maxActive,
		    @Value("${database.pool.min.idle}") int minIdle, @Value("${database.pool.max.age}") int maxAge,
		    @Value("${database.test.borrow}") boolean testOnBorrow,
		    @Value("${database.test.idle}") boolean testOnReturn,
		    @Value("${database.pool.max.idle}") int maxIdle,
		    @Value("${database.test.return}") boolean testWhileIdle,
		    @Value("${database.validation.query}") String validationQuery,
		    @Value("${database.validation.interval}") long validationInterval,
		    @Value("${database.validation.timeout}") int validationQueryTimeout,
		    @Value("${database.eviction.time}") int timeBetweenEvictionRunsMillis,
		    @Value("${database.eviction.idle}") int minEvictableIdleTimeMillis,
		    @Value("StatementCache(prepared=true,callable=true,max=10000)") String jdbcInterceptors) {
	DataSource dataSource = new DataSource();
	dataSource.setDriverClassName(driverClassName);
	dataSource.setUrl(url);
	dataSource.setUsername(username);
	dataSource.setPassword(password);
	dataSource.setInitialSize(initialSize);
	dataSource.setMaxActive(maxActive);
	dataSource.setMinIdle(minIdle);
	dataSource.setMaxAge(maxAge);
	dataSource.setTestOnBorrow(testOnBorrow);
	dataSource.setTestOnReturn(testOnReturn);
	dataSource.setMaxIdle(maxIdle);
	dataSource.setTestWhileIdle(testWhileIdle);
	dataSource.setValidationQuery(validationQuery);
	dataSource.setValidationInterval(validationInterval);
	dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
	dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
	dataSource.setValidationQueryTimeout(validationQueryTimeout);
	dataSource.setJdbcInterceptors(jdbcInterceptors);
	return dataSource;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
	JdbcTemplate jdbcTemplate = new JdbcTemplate();
	jdbcTemplate.setDataSource(dataSource);
	return jdbcTemplate;
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
	DataSourceTransactionManager manager = new DataSourceTransactionManager();
	manager.setDataSource(dataSource);
	return manager;
    }

    @Bean(name = "transactionTemplate")
    public TransactionTemplate transactionTemplate(DataSourceTransactionManager transactionManager) {
	TransactionTemplate template = new TransactionTemplate();
	template.setTransactionManager(transactionManager);
	return template;
    }

    @Bean(name = "personService")
    public PersonService personService() {
	PersonService ps = new PersonService();
	return ps;
    }

    @Bean(name = "queryService")
    public QueryService queryService() {
	return QueryService.getInstance();
    }

    @Bean(name = "fileSystemService")
    public FileSystemService fileSystemService(@Value("${root.folder.path}") String rootFolderPath)
		    throws InternalErrorException {
	FileSystemService fss = new FileSystemService();
	fss.setRootFolder(rootFolderPath);
	return fss;
    }

    @Bean(name = "registerPersonProcessor")
    public RegisterPersonProcessor registerPersonProcessor() {
	return new RegisterPersonProcessor();
    }

    @Bean(name = "dBFolderService")
    public DBFolderService dBFolderService() {
	return new DBFolderService();
    }

    @Bean(name = "createFolderProcessor")
    public CreateFolderProcessor createFolderProcessor() {
	return new CreateFolderProcessor();
    }

    @Bean(name = "addFileProcessor")
    public AddFileProcessor addFileProcessor() {
	return new AddFileProcessor();
    }

    @Bean(name = "dBFileService")
    public DBFileService dBFileService() {
	return new DBFileService();
    }
}