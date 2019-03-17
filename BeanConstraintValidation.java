package com.security.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
    Retention Policy specifies when the annotation expires. There are 3 available options:

    SOURCE - these annotations are just for show. They are discarded by the compiler
    CLASS - default value. The annotations are stored by the compiler in the class files
    but not available for runtime. Good for doing compile time checks.
    RUNTIME - the annotation is not discarded. Its available during runtime to be queried by
    reflection.

    All members of an annotations MUST be methods!

    We can provide default values for the annotation variable using the default keyword. Only constraint
    here is that the default value provided MUST match the type being returned by the function

    type methodName() default same-typed-value

    String something() default "abc";
    int[] something() default {}; //for array types
*/

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartsWithValidator.class)
@Target(ElementType.FIELD)
public @interface StartsWith {
    String message() default "Value of the field does not start with a valid character!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] value() default {}; //using the 'value' field name allows for omitting this field name when using the annotation(only when value is being specified)
}


package com.security.annotation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartsWithValidator implements ConstraintValidator<StartsWith,String> {

    private String[] validStartsWithValues;

    /*
        We can use this function to grab the run time parameters of the annotation
    */
    @Override
    public void initialize(StartsWith constraintAnnotation) {
        //get the list of valid values for the current instance of the annotation
        this.validStartsWithValues = constraintAnnotation.value();
    }

    /*
    *   Runs the actual validation here.
    *   @param s represents the passed in value for the field we are checking
    * */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if( !StringUtils.isEmpty(s) )
            for( String startWithVal : validStartsWithValues ) {
                if( StringUtils.startsWithIgnoreCase(s, startWithVal) )
                    return true;
            }
        return false;
    }
}

package com.security.annotation;

public class StartsWithTest {

    //these are 'marker' interfaces that serves only to mark annotations for grouping
    public interface FirstNameGroup {}
    public interface LastNameGroup {}

    @StartsWith(value={"Anton", "Peprika"}, groups = { FirstNameGroup.class })
    private String firstName;

    @StartsWith(value={"De Silva", "Perera"}, groups = { LastNameGroup.class })
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}


package com.security;

import com.security.annotation.StartsWithTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@SpringBootApplication
@EnableWebSecurity
public class Application {
    public static void main(String[] args)
    {
        StartsWithTest test = new StartsWithTest();
        test.setFirstName("Peprika");
        test.setLastName("DeSilva");

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        //by specifying the group names, we can control both the order and the execution(i.e. should validation run or not)
        //of the validations
        Set<ConstraintViolation<StartsWithTest>> startsWithViolations = validator.validate(
                test,
                StartsWithTest.LastNameGroup.class, //runs the validation on 'StartsWith' annotated fields whose groups include LastNameGroup
                StartsWithTest.FirstNameGroup.class ); //runs the validation on 'StartsWith' annotated fields whose groups include FirstNameGroup

        for(ConstraintViolation<StartsWithTest> violation : startsWithViolations )
        {
            System.out.println( violation );
        }
    }
}