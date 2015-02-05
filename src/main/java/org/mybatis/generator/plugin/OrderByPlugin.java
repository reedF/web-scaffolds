package org.mybatis.generator.plugin;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * <pre>
 * all fields order by
 * This class is only used in ibator code generator.
 * 
 * ex: XXXExample.orderByXXX(true).orderByXXX(false);
 * </pre>
 */
public class OrderByPlugin extends PluginAdapter {
	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		// add field, getter, setter for order by
		addOrderBy(topLevelClass, introspectedTable);

		return super.modelExampleClassGenerated(topLevelClass,
				introspectedTable);
	}

	private void addOrderBy(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
		if (columns != null && columns.size() > 0) {
			for (IntrospectedColumn c : columns) {
				if (c != null) {
					String propertyName = c.getJavaProperty();
					String columnName = c.getActualColumnName();
					makeUpOrderBy(topLevelClass, introspectedTable,
							propertyName, columnName);
				}
			}
		}

	}

	private void makeUpOrderBy(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable, String propertyName,
			String columnName) {
		char c = propertyName.charAt(0);
		String camel = Character.toUpperCase(c) + propertyName.substring(1);
		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setName("orderBy" + camel);
		method.setReturnType(new FullyQualifiedJavaType(introspectedTable
				.getExampleType()));
		method.addParameter(new Parameter(FullyQualifiedJavaType
				.getBooleanPrimitiveInstance(), "isDesc"));
		method.addBodyLine(
				0,
				"if (this.orderByClause != null) {this.orderByClause += \",\";} else {this.orderByClause=\"\";}");
		method.addBodyLine(1, "this.orderByClause += (isDesc == true) ?\""
				+ columnName + " DESC \" : \"" + columnName + "\";");
		method.addBodyLine(2, "return this ;");
		topLevelClass.addMethod(method);
	}

	/**
	 * This plugin is always valid - no properties are required
	 */
	public boolean validate(List<String> warnings) {
		return true;
	}

	public static void generate() {
		String config = OrderByPlugin.class.getClassLoader()
				.getResource("mybatisConfig.xml").getFile();
		String[] arg = { "-configfile", config, "-overwrite" };
		ShellRunner.main(arg);
	}

	public static void main(String[] args) {
		generate();
	}
}
