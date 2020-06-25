package com.github.raonigabriel.imdb;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.GeneralCodingRules.ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;

import java.io.Externalizable;
import java.io.Serializable;

import javax.persistence.Entity;

import org.slf4j.Logger;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

@AnalyzeClasses(packages = "com.github.raonigabriel.imdb")
public class ArchitectureTest {

	@ArchTest
	private final ArchRule no_generic_exceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

	@ArchTest
	private final ArchRule no_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

	@ArchTest
	private final ArchRule no_jodatime = NO_CLASSES_SHOULD_USE_JODATIME;

	@ArchTest
	private final ArchRule no_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
	
	@ArchTest
	private final ArchRule no_access_to_standard_streams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
	
	@ArchTest
	private final ArchRule entities_on_same_package = classes().that()
		.areAnnotatedWith(Entity.class)
		.should().resideInAPackage("com.github.raonigabriel.imdb.model")
		.because("All entities should be in the same package");

	@ArchTest
	private final ArchRule externalizable_version_control =  classes().that()
		.implement(Externalizable.class).should().implement(Serializable.class)
		.andShould(HANDLE_VERSION_PROPERLY);

	
	@ArchTest
	private void no_access_to_standard_streams_as_method(JavaClasses classes) {
		noClasses().should(ACCESS_STANDARD_STREAMS).check(classes);
	}

	@ArchTest
	private final ArchRule loggers_should_be_private_static_final = fields().that()
		.haveRawType(Logger.class)
		.should().bePrivate()
		.andShould().beStatic()
		.andShould().beFinal()
		.because("we agreed on this convention");
	
	private static final ArchCondition<? super JavaClass> HANDLE_VERSION_PROPERLY = 
			new ArchCondition<JavaClass>("have a public static final field named VERSION of type long") {
				@Override
				public void check(JavaClass item, ConditionEvents events) {
		        	if (!item.getAllFields().stream().anyMatch(f -> {
		        		return "VERSION".equals(f.getName()) 
		        				&& f.getModifiers().contains(JavaModifier.PUBLIC)
		        				&& f.getModifiers().contains(JavaModifier.STATIC)
		        				&& f.getModifiers().contains(JavaModifier.FINAL)
		        				&&f.getRawType().isAssignableFrom(Long.TYPE);
		        	})) {
		        		String message = "public static final long VERSION was not found on " + item.getName();
		        		events.add(SimpleConditionEvent.violated(item, message));
		        	}
					
				}
		
	};
	
}
