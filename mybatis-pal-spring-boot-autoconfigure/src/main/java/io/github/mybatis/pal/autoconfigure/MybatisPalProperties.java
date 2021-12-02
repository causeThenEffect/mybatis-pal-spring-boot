package io.github.mybatis.pal.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author cause
 * @date 2021/12/2
 */
@ConfigurationProperties(prefix = MybatisPalProperties.MYBATIS_PREFIX)
public class MybatisPalProperties {

  public static final String MYBATIS_PREFIX = "mybatis.pal";

  private String limitMilliSecond;

  public String getLimitMilliSecond() {
    return limitMilliSecond;
  }

  public void setLimitMilliSecond(String limitMilliSecond) {
    this.limitMilliSecond = limitMilliSecond;
  }

}
