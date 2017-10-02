package kharkov.kp.gic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

/*
//create folder
mkdir -p ~/data/redis
//start Redis server
sudo docker run --name my-redis-db -p 6379:6379 -v ~/data/redis:/data -d redis redis-server --appendonly yes  
//run Redis client
sudo docker run -it --link my-redis-db:redis --rm redis redis-cli -h redis -p 6379
//remove Redis container
sudo docker rmi my-redis-db
*/

public class RedisConfiguration {
	
	@Value("${spring.redis.database}")
	private int dateBase;
	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int port;
	@Value("${spring.redis.password}")
	private String password;
	@Value("${spring.redis.pool.max-active}")
	private int maxTotal;
	@Value("${spring.redis.pool.max-wait}")
	private int maxWait;
	@Value("${spring.redis.pool.max-idle}")
	private int maxIdle;
	@Value("${spring.redis.pool.min-idle}")
	private int minIdle;
	@Value("${spring.redis.timeout}")
	private int timeout;

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setDatabase(dateBase);
		factory.setHostName(host);
		factory.setPort(port);
		factory.setPassword(password);
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setMinIdle(minIdle);
		poolConfig.setMaxWaitMillis(maxWait);
		poolConfig.setMaxTotal(maxTotal);
		factory.setPoolConfig(poolConfig);
		factory.setTimeout(timeout);
		return factory;
	}
	
	@Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        return template;
    }
	
}
