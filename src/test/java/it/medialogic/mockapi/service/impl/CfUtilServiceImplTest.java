package it.medialogic.mockapi.service.impl;

import it.medialogic.mockapi.exception.CustomException;
import it.medialogic.mockapi.service.CfUtilService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@Slf4j
public class CfUtilServiceImplTest {

    @TestConfiguration
    public static class CfUtilServiceConfiguration {
        @Bean
        public CfUtilService CfUtilService() {
            return new CfUtilServiceImpl();
        }

    }

    @Autowired
    public CfUtilService cfUtilService;

    public String cf;

    @Test
    public void itShouldRecognizeValidCf() {
        cf = "MRNDRD97C01H501S";
        assertThat(cfUtilService.checkCf(cf)).isTrue();
    }

    @Test(expected = CustomException.class)
    public void itShouldThrowLengthError() {
        cf = "MRNDRD97C01H501SFG";
        cfUtilService.checkCf(cf);
    }

    // todo: in the future it will return wrong value because you can grow
    @Test
    public void itShouldReturnTheRightAge() {
        cf = "MRNDRD97C01H501S";
        assertThat(cfUtilService.getAge(cf)).isEqualTo(27);
    }

    @Test
    public void itShouldReturnTheRightBirthday() {
        cf = "MRNDRD97C01H501S";
        assertThat(cfUtilService.getBirth(cf)).isEqualTo("01-03-97");
    }

}