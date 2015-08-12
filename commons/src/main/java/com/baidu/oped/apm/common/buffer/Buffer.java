
package com.baidu.oped.apm.common.buffer;

/**
 * class Buffer 
 *
 * @author meidongxu@baidu.com
 */
public interface Buffer {

    int BOOLEAN_FALSE = 0;
    int BOOLEAN_TRUE = 1;
    byte[] EMPTY = new byte[0];
    String UTF8 = "UTF-8";

    void putPadBytes(byte[] bytes, int totalLength);

    void putPrefixedBytes(byte[] bytes);

    void put2PrefixedBytes(byte[] bytes);

    void put4PrefixedBytes(byte[] bytes);

    void putPadString(String string, int totalLength);

    void putPrefixedString(String string);

    void put2PrefixedString(String string);

    void put4PrefixedString(String string);

    void put(byte v);

    void put(boolean v);

    void put(int v);

    /**
     * put value using the variable-length encoding especially for constants
     * the size using variable-length encoding is bigger than using fixed-length int when v is negative.
     * if there are a lot of negative value in a buffer, it's very inefficient.
     * instead use putSVar in that case.
     * putVar compared to putSVar has a little benefit to use a less cpu due to no zigzag operation.
     * it has more benefit to use putSVar whenever v is negative.
     * consume 1~10 bytes ( integer's max value consumes 5 bytes, integer's min value consumes 10 bytes)
     * @param v
     */
    void putVar(int v);

    /**
     * put value using variable-length encoding
     * useful for same distribution of constants and negatives value
     * consume 1~5 bytes ( integer's max value consumes 5 bytes, integer's min value consumes 5 bytes)

     * @param v
     */
    void putSVar(int v);

    void put(short v);

    void put(long v);

    /**
     * put value using the variable-length encoding especially for constants
     * the size using variable-length encoding is bigger than using fixed-length int when v is negative.
     * if there are a lot of negative value in a buffer, it's very inefficient.
     * instead use putSVar in that case.
     * @param v
     */
    void putVar(long v);

    /**
     * put value using variable-length encoding
     * useful for same distribution of constants and negatives value
     * @param v
     */
    void putSVar(long v);

    void put(double v);

    /**
     * put value using the variable-length encoding especially for constants
     * the size using variable-length encoding is bigger than using fixed-length int when v is negative.
     * if there are a lot of negative value in a buffer, it's very inefficient.
     * instead use putSVar in that case.
     * @param v
     */
    void putVar(double v);

    /**
     * put value using variable-length encoding
     * useful for same distribution of constants and negatives value
     * @param v
     */
    void putSVar(double v);

    void put(byte[] v);

    byte readByte();

    int readUnsignedByte();

    boolean readBoolean();

    int readInt();

    int readVarInt();

    int readSVarInt();


    short readShort();

    long readLong();

    long readVarLong();

    long readSVarLong();

    double readDouble();

    double readVarDouble();

    double readSVarDouble();

    byte[] readPadBytes(int totalLength);

    String readPadString(int totalLength);

    String readPadStringAndRightTrim(int totalLength);

    byte[] readPrefixedBytes();

    byte[] read2PrefixedBytes();

    byte[] read4PrefixedBytes();

    String readPrefixedString();

    String read2PrefixedString();

    String read4PrefixedString();

    byte[] getBuffer();

    byte[] copyBuffer();

    byte[] getInternalBuffer();

    void setOffset(int offset);

    int getOffset();

    int limit();
}
