import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.service.ExtentTestManager;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static io.qameta.allure.util.AspectUtils.getName;
import static io.qameta.allure.util.AspectUtils.getParameters;

/**
 * AspectJ implementation class the extends teh allure report implementation to extent reporting and
 * logging
 *
 * @author Uvaraj Krishnaswamy
 * @version 1.0
 * @since 6/24/2019
 */
@Aspect
public class StepsAspects {

    /**
     * A point cut method to narrow down on Step annotation
     *
     * @author Uvaraj Krishnaswamy
     * @since 6/24/2019
     */
    @Pointcut("@annotation(io.qameta.allure.Step)")
    public void withStepAnnotation() {
        //pointcut body, should be empty
    }

    /**
     * A point cut method to allow execution from any method invocation
     *
     * @author Uvaraj Krishnaswamy
     * @since 6/24/2019
     */
    @Pointcut("execution(* *(..))")
    public void anyMethod() {
        //pointcut body, should be empty
    }

    /**
     * A before method which is executed before any method annotated with Step
     *
     * @param joinPoint provides accessibility towards the step annotation values
     * @author Uvaraj Krishnaswamy
     * @since 6/24/2019
     */
    @Before("anyMethod() && withStepAnnotation()")
    public void stepStart(final JoinPoint joinPoint) {
        try {
            final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
            String name = getStepText(joinPoint);
            List<Parameter> parameters = getParameters(methodSignature, joinPoint.getArgs());
            parameters.forEach(parameter -> {
                logger.trace("{} = {}", parameter.getName(), parameter.getValue());
            });
            logger.trace("Starting : {}", name);
        } catch (Throwable e) {
            //ignore exception
        }
    }

    private String getStepText(JoinPoint joinPoint) {
        String name;
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Step step = methodSignature.getMethod().getAnnotation(Step.class);
        name = getName(step.value(), methodSignature, joinPoint.getArgs());
        return name;
    }

    /**
     * Executed if the method failed
     *
     * @param e Exception thrown by the method
     * @author Uvaraj Krishnaswamy
     * @since 6/24/2019
     */
    @AfterThrowing(pointcut = "anyMethod() && withStepAnnotation()", throwing = "e")
    public void stepFailed(final JoinPoint joinPoint, final Throwable e) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
        String name = getStepText(joinPoint);
        try {
            ExtentTest test = ExtentTestManager.getTest();
            if (Objects.nonNull(test)) {
                test.fail(name);
            }
            logger.error(name);
        } catch (Throwable ex) {
            //ignore exception
        }
    }

    /**
     * Executed if the method is successful without any exception
     *
     * @author Uvaraj Krishnaswamy
     * @since 6/24/2019
     */
    @AfterReturning(pointcut = "anyMethod() && withStepAnnotation()")
    public void stepStop(final JoinPoint joinPoint) {
        Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
        String name = getStepText(joinPoint);
        try {
            ExtentTest test = ExtentTestManager.getTest();
            if (Objects.nonNull(name)) {
                test.info(name);
            }
            logger.info(name);
        } catch (Throwable e) {
            //ignore exception
        }
    }

}