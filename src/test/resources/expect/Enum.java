/*
Please do not modify this file.
This is generated code.
*/

package com.example;

import java.util.Objects;
import java.util.Optional;


/**
 * Some common transport layer protocols.
 */
public enum TransportProtocol
{

    /**
     * Transmission Control Protocol
     */
    TCP("TCP (Transmission Control Protocol)", 0x06, 20),

    /**
     * User Datagram Protocol
     */
    UDP("UDP (User Datagram Protocol)", 0x11, 8);

    private final String displayName;
    private final int number;
    private final int headerLength;

    /**
     * @param number The protocol number used in the IPv4 header
     * @param headerLength The typical number of octets in a packet header
     */
    private TransportProtocol(String displayName, int number, int headerLength)
    {
        this.displayName = displayName;
        this.number = number;
        this.headerLength = headerLength;
    }

    /**
     * @return The protocol number used in the IPv4 header
     */
    public int getNumber()
    {
        return number;
    }

    /**
     * @return The typical number of octets in a packet header
     */
    public int getHeaderLength()
    {
        return headerLength;
    }

    @Override
    public String toString()
    {
        return this.displayName;
    }

    /**
     * Retrieves the transport protocol that matches a given string.
     *
     * @param str the protocol name (acronym) to resolve
     * @return the matching protocol, or an empty Optional if there is none
     */
    public static Optional<TransportProtocol> get(String str)
    {
        Objects.requireNonNull(str, "str");
        switch (str.toLowerCase().trim())
        {
            case "tcp":
                return Optional.of(TCP);
            case "udp":
                return Optional.of(UDP);
            default:
                return Optional.empty();
        }
    }
}