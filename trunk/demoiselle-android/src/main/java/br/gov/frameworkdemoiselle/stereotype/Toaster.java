package br.gov.frameworkdemoiselle.stereotype;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Stereotype to Toaster Messages. Annotate all injections with this annotation
 * to use Toaster Message Implementation.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
@Inherited
@Target(FIELD)
@Retention(RUNTIME)
@BindingAnnotation
public @interface Toaster {

}
