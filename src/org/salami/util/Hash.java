package org.salami.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

/**
 * A class that represents the result of a hashing function, such as <tt>MD5</tt>, <tt>SHA-1</tt>, etc.
 * 
 * @author Daniel Escoz
 * @version 1.0
 */
public final class Hash {
	
	/** Type of this hash */
	private final String type;
	
	/** Bytes of the hash */
	private final byte[] bytes;
	
	/** Hexadecimal representation of <tt>bytes</tt> */
	private final String hexBytes;
	
	/** Cached <tt>hashCode()</tt>value */
	private final int hashCode;
	
	/**
	 * Constructs a <tt>Hash</tt> of the specified <tt>type</tt> using all bytes of the passed <tt>bytes</tt> array.
	 * 
	 * @param type
	 *            The type of this hash
	 * @param bytes
	 *            The bytes of this hash
	 */
	public Hash ( final String type, final byte[] bytes ) {
		this( type, bytes, 0, bytes.length );
	}
	
	/**
	 * Constructs a <tt>Hash</tt> of the specified <tt>type</tt> using bytes of the passed <tt>bytes</tt> array that are
	 * in positions greater or equal to <tt>from</tt> and strictly lower than <tt>to</tt>.
	 * 
	 * @param type
	 *            The type of this hash
	 * @param bytes
	 *            The bytes of this hash
	 * @param from
	 *            the initial index of the range to be copied, inclusive
	 * @param the
	 *            final index of the range to be copied, exclusive.
	 * @see java.util.Arrays#copyOfRange(byte[], int, int)
	 */
	public Hash ( final String type, final byte[] bytes, final int from, final int to ) {
		this.type = Objects.requireNonNull( type, "type" ).toUpperCase();
		this.bytes = Arrays.copyOfRange( bytes, from, to );
		
		// Cached hashCode()value
		hashCode = Arrays.hashCode( this.bytes ) ^ this.type.hashCode();
		
		// Cached hex string representation
		final StringBuilder sb = new StringBuilder();
		for ( final byte b : this.bytes ) {
			final int ib = b & 0xFF;
			
			if ( ib < 16 ) {
				sb.append( '0' );
			}
			
			sb.append( Integer.toHexString( ib ) );
		}
		hexBytes = sb.toString();
	}
	
	/** @return The type of this hash */
	public String getType () {
		return type;
	}
	
	/** @return A fresh array containing only the bytes of this hash */
	public byte[] getBytes () {
		return Arrays.copyOf( bytes, bytes.length );
	}
	
	/** @return A hexadecimal representation of this hash */
	public String getHexBytes () {
		return hexBytes;
	}
	
	@Override
	public String toString () {
		return type + "(" + hexBytes + ")";
	}
	
	@Override
	public int hashCode () {
		return hashCode;
	}
	
	@Override
	public boolean equals ( final Object obj ) {
		if ( !( obj instanceof Hash ) ) {
			return false;
		}
		
		final Hash hash = (Hash) obj;
		return type.equals( hash.type ) && Arrays.equals( bytes, hash.bytes );
	}
	
	/**
	 * Hashes the contents of a file using the specified <tt>algorithm</tt> and returns a <tt>Hash</tt> object
	 * representing it. The <tt>type</tt> attribute of the returned <tt>Hash</tt> is the name of the algorithm passed to
	 * this method.
	 * 
	 * @param algorithm
	 *            Hashing algorithm to use
	 * @param path
	 *            Path to the file to hash
	 * @return The contents of the file hashed with the specified algorithm
	 * @throws NoSuchAlgorithmException
	 *             If the specified algorithm is not supported
	 * @throws IOException
	 *             If an I/O error occurs while reading the file
	 * @see java.security.MessageDigest
	 */
	public static Hash forFile ( final String algorithm, final Path path ) throws NoSuchAlgorithmException, IOException
	{
		final MessageDigest md = MessageDigest.getInstance( algorithm.toUpperCase() );
		try ( InputStream in = Files.newInputStream( path, StandardOpenOption.READ ) ) {
			final byte[] buffer = new byte[ 4 * 1024 ];
			
			int read;
			while ( ( read = in.read( buffer, 0, buffer.length ) ) > 0 ) {
				md.update( buffer, 0, read );
			}
		}
		
		return new Hash( md.getAlgorithm(), md.digest() );
	}
}
