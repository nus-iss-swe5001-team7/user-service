package com.nus.edu.se.user_service.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EurekaConfigTest {

    @InjectMocks
    private EurekaConfig eurekaConfig;

    @Mock
    private InetUtils inetUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Set serverPort value (mocks the @Value annotation behavior)
        eurekaConfig.serverPort = 8080;
    }

    @Test
    void testEurekaInstanceConfig_Success() throws UnknownHostException {
        // Arrange
        InetUtils.HostInfo mockHostInfo = mock(InetUtils.HostInfo.class);
        when(mockHostInfo.getIpAddress()).thenReturn("192.168.1.1");
        when(inetUtils.findFirstNonLoopbackHostInfo()).thenReturn(mockHostInfo);

        try (var mockedInetAddress = Mockito.mockStatic(InetAddress.class)) {
            InetAddress mockInetAddress = mock(InetAddress.class);
            mockedInetAddress.when(InetAddress::getLocalHost).thenReturn(mockInetAddress);
            when(mockInetAddress.getHostAddress()).thenReturn("192.168.1.1");

            // Act
            EurekaInstanceConfigBean config = eurekaConfig.eurekaInstanceConfig(inetUtils);

            // Assert
            assertNotNull(config);
            assertEquals("192.168.1.1", config.getIpAddress());
            assertEquals(8080, config.getNonSecurePort());
            assertTrue(config.isNonSecurePortEnabled());
            assertTrue(config.isPreferIpAddress());
        }
    }

    @Test
    void testEurekaInstanceConfig_UnknownHostException() throws UnknownHostException {
        // Arrange
        InetUtils.HostInfo mockHostInfo = mock(InetUtils.HostInfo.class);
        when(mockHostInfo.getIpAddress()).thenReturn(null); // Simulate no IP
        when(inetUtils.findFirstNonLoopbackHostInfo()).thenReturn(mockHostInfo);

        try (var mockedInetAddress = Mockito.mockStatic(InetAddress.class)) {
            mockedInetAddress.when(InetAddress::getLocalHost).thenThrow(new UnknownHostException("Unable to resolve host"));

            // Act
            EurekaInstanceConfigBean config = eurekaConfig.eurekaInstanceConfig(inetUtils);

            // Assert
            assertNotNull(config);
            assertNull(config.getIpAddress()); // IP should be null due to exception
            assertEquals(8080, config.getNonSecurePort());
            assertTrue(config.isNonSecurePortEnabled());
            assertTrue(config.isPreferIpAddress());
        }
    }


}
