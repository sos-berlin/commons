/*
 * SequenceReader.java Created on 07.08.2007
 */
package sos.connection;

/** this interface can be implemented by SOSConnection classes for databases
 * which provide sequences
 *
 * @author Andreas Liebert */
public interface SequenceReader {

    /** Fetches the next value of a sequence
     * 
     * @param sequence name of the sequence
     * @return next value of the sequence
     * @throws Exception */
    public String getNextSequenceValue(String sequence) throws Exception;
}
