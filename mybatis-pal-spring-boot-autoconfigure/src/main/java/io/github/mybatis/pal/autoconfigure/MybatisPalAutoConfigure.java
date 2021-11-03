package io.github.mybatis.pal.autoconfigure;

import io.github.mybatis.pal.CosumeTimeInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
@Lazy(false)
public class MybatisPalAutoConfigure implements InitializingBean {

  @Autowired
  private List<SqlSessionFactory> sqlSessionFactoryList;

  @Override
  public void afterPropertiesSet() throws Exception {

    CosumeTimeInterceptor interceptor = new CosumeTimeInterceptor();
//    Properties properties = new Properties();
//    先把一般方式配置的属性放进去
//    properties.putAll(pageHelperProperties());
//    在把特殊配置放进去，由于close-conn 利用上面方式时，属性名就是 close-conn 而不是 closeConn，所以需要额外的一步
//    properties.putAll(this.properties.getProperties());
//    interceptor.setProperties(properties);
    for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
      org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
      if (!containsInterceptor(configuration, interceptor)) {
        configuration.addInterceptor(interceptor);
      }
    }
  }

  /**
   * 是否已经存在相同的拦截器
   *
   * @param configuration
   * @param interceptor
   * @return
   */
  private boolean containsInterceptor(org.apache.ibatis.session.Configuration configuration, Interceptor interceptor) {
    try {
      // getInterceptors since 3.2.2
      return configuration.getInterceptors().contains(interceptor);
    } catch (Exception e) {
      return false;
    }
  }

}
