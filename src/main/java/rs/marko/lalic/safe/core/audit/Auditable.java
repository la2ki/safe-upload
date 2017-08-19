package rs.marko.lalic.safe.core.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used in {@link AuditInterceptor} to determine if event should be audited
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //can use in method only.
public @interface Auditable {

    /**
     * Represents event name
     */
    String value();
}
