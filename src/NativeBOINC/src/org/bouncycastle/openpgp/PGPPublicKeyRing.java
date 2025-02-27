package org.bouncycastle.openpgp;

import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.io.OutputStream;
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Iterator;
import java.util.List;

import org.bouncycastle.bcpg.BCPGInputStream;
import org.bouncycastle.bcpg.PacketTags;
import org.bouncycastle.bcpg.PublicKeyPacket;
import org.bouncycastle.bcpg.TrustPacket;

/**
 * Class to hold a single master public key and its subkeys.
 * <p>
 * Often PGP keyring files consist of multiple master keys, if you are trying to process
 * or construct one of these you should use the PGPPublicKeyRingCollection class.
 */
public class PGPPublicKeyRing
    extends PGPKeyRing
{
    List keys;
    
    public PGPPublicKeyRing(
        byte[]    encoding)
        throws IOException
    {
        this(new ByteArrayInputStream(encoding));
    }
    
    /**
     * @param pubKeys
     */
    PGPPublicKeyRing(
        List pubKeys)
    {
        this.keys = pubKeys;
    }

    public PGPPublicKeyRing(
        InputStream    in)
        throws IOException
    {
        this.keys = new ArrayList();

        BCPGInputStream pIn = wrap(in);

        int initialTag = pIn.nextPacketTag();
        if (initialTag != PacketTags.PUBLIC_KEY && initialTag != PacketTags.PUBLIC_SUBKEY)
        {
            throw new IOException(
                "public key ring doesn't start with public key tag: " +
                "tag 0x" + Integer.toHexString(initialTag));
        }

        PublicKeyPacket pubPk = (PublicKeyPacket)pIn.readPacket();
        TrustPacket     trustPk = readOptionalTrustPacket(pIn);

        // direct signatures and revocations
        List keySigs = readSignaturesAndTrust(pIn);

        List ids = new ArrayList();
        List idTrusts = new ArrayList();
        List idSigs = new ArrayList();
        readUserIDs(pIn, ids, idTrusts, idSigs);

        keys.add(new PGPPublicKey(pubPk, trustPk, keySigs, ids, idTrusts, idSigs));


        // Read subkeys
        while (pIn.nextPacketTag() == PacketTags.PUBLIC_SUBKEY)
        {
            keys.add(readSubkey(pIn));
        }
    }

    /**
     * Return the first public key in the ring.
     * 
     * @return PGPPublicKey
     */
    public PGPPublicKey getPublicKey()
    {
        return (PGPPublicKey)keys.get(0);
    }
    
    /**
     * Return the public key referred to by the passed in keyID if it
     * is present.
     * 
     * @param keyID
     * @return PGPPublicKey
     */
    public PGPPublicKey getPublicKey(
        long        keyID)
    {    
        for (int i = 0; i != keys.size(); i++)
        {
            PGPPublicKey    k = (PGPPublicKey)keys.get(i);
            
            if (keyID == k.getKeyID())
            {
                return k;
            }
        }
    
        return null;
    }
    
    /**
     * Return an iterator containing all the public keys.
     * 
     * @return Iterator
     */
    /*public Iterator getPublicKeys()
    {
        return Collections.unmodifiableList(keys).iterator();
    }*/
    
    /*public byte[] getEncoded() 
        throws IOException
    {
        ByteArrayOutputStream    bOut = new ByteArrayOutputStream();
        
        this.encode(bOut);
        
        return bOut.toByteArray();
    }*/
    
    /*public void encode(
        OutputStream    outStream) 
        throws IOException
    {
        for (int i = 0; i != keys.size(); i++)
        {
            PGPPublicKey    k = (PGPPublicKey)keys.get(i);
            
            k.encode(outStream);
        }
    }*/
    
    /**
     * Returns a new key ring with the public key passed in
     * either added or replacing an existing one.
     * 
     * @param pubRing the public key ring to be modified
     * @param pubKey the public key to be inserted.
     * @return a new keyRing
     */
    /*public static PGPPublicKeyRing insertPublicKey(
        PGPPublicKeyRing  pubRing,
        PGPPublicKey      pubKey)
    {
        List       keys = new ArrayList(pubRing.keys);
        boolean    found = false;
        boolean    masterFound = false;

        for (int i = 0; i != keys.size();i++)
        {
            PGPPublicKey   key = (PGPPublicKey)keys.get(i);
            
            if (key.getKeyID() == pubKey.getKeyID())
            {
                found = true;
                keys.set(i, pubKey);
            }
            if (key.isMasterKey())
            {
                masterFound = true;
            }
        }

        if (!found)
        {
            if (pubKey.isMasterKey())
            {
                if (masterFound)
                {
                    throw new IllegalArgumentException("cannot add a master key to a ring that already has one");
                }

                keys.add(0, pubKey);
            }
            else
            {
                keys.add(pubKey);
            }
        }
        
        return new PGPPublicKeyRing(keys);
    }*/
    
    /**
     * Returns a new key ring with the public key passed in
     * removed from the key ring.
     * 
     * @param pubRing the public key ring to be modified
     * @param pubKey the public key to be removed.
     * @return a new keyRing, null if pubKey is not found.
     */
    /*public static PGPPublicKeyRing removePublicKey(
        PGPPublicKeyRing  pubRing,
        PGPPublicKey      pubKey)
    {
        List       keys = new ArrayList(pubRing.keys);
        boolean    found = false;
        
        for (int i = 0; i < keys.size();i++)
        {
            PGPPublicKey   key = (PGPPublicKey)keys.get(i);
            
            if (key.getKeyID() == pubKey.getKeyID())
            {
                found = true;
                keys.remove(i);
            }
        }
        
        if (!found)
        {
            return null;
        }
        
        return new PGPPublicKeyRing(keys);
    }*/

    static PGPPublicKey readSubkey(BCPGInputStream in)
        throws IOException
    {
        PublicKeyPacket pk = (PublicKeyPacket)in.readPacket();
        TrustPacket     kTrust = readOptionalTrustPacket(in);

        // PGP 8 actually leaves out the signature.
        List sigList = readSignaturesAndTrust(in);

        return new PGPPublicKey(pk, kTrust, sigList);
    }
}
