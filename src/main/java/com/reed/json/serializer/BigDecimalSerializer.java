package com.reed.json.serializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.fasterxml.jackson.databind.JsonSerializer;

/**
 * 自定义返回JSON 数据格中BigDecimal格式化处理，使用方法：在domain的property上使用注解：
 * 
 * @JsonSerialize(using = BigDecimalSerializer.class)
 * @NumberFormat(style = Style.NUMBER, pattern = BigDecimalSerializer.style)
 * 
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

	public static final String style = "#######################";

	@Override
	public void serialize(BigDecimal value,
			com.fasterxml.jackson.core.JsonGenerator gen,
			com.fasterxml.jackson.databind.SerializerProvider serializers)
			throws IOException,
			com.fasterxml.jackson.core.JsonProcessingException {
		DecimalFormat df = new DecimalFormat(style);
		String s = df.format(value);
		gen.writeString(s);

	}

}
