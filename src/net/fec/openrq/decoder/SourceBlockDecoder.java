/*
 * Copyright 2014 Jose Lopes
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fec.openrq.decoder;


import java.util.Set;

import net.fec.openrq.EncodingPacket;
import net.fec.openrq.parameters.ParameterChecker;


/**
 * A decoder for a source block.
 * <p>
 * A source block decoder is retrieved from a {@link DataDecoder} object, which is associated to some source data.
 * Source data is divided into source blocks and each source block is independently encoded by a RaptorQ encoder (as
 * specified in RFC 6330). Each source block is further divided into source symbols, which together with repair symbols
 * (extra encoded data) form the <em>encoding symbols</em>. The encoding symbols are transmitted inside encoding packets
 * to an instance of this interface.
 * <p>
 * A source block can be decoded independently by an instance of this interface, and the block is identified by a source
 * block number, which is carried inside an encoding packet. The method {@link #sourceBlockNumber()} provides the source
 * block number that identifies the source block being decoded. Additionally, the number of source symbols into which
 * the source block is divided is given by the method {@link #numberOfSourceSymbols()}.
 * <p>
 * The method {@link #putEncodingPacket(EncodingPacket)} receives an encoding packet as argument and stores the encoding
 * symbols inside it for future decoding. If at the time the method is called, enough symbols are available for decoding
 * the source block, then a decoding operation takes place which either succeeds or not (a decoding failure).
 * <p>
 * Handling decoding failures is a task for the user. Typically, the user requests the sender for any missing source
 * symbols or simply waits for more encoding symbols (source or repair) to be available. The method
 * {@link #missingSourceSymbols()} returns a set with the identifiers of all missing source symbols, and the method
 * {@link #availableRepairSymbols()} returns a set with the identifiers of all available repair symbols so far.
 */
public interface SourceBlockDecoder {

    /**
     * Returns the data decoder object from which this source block decoder was retrieved.
     * 
     * @return the data decoder object from which this source block decoder was retrieved
     */
    public DataDecoder dataDecoder();

    /**
     * Returns the source block number for the source block being decoded.
     * 
     * @return the source block number for the source block being decoded
     */
    public int sourceBlockNumber();

    /**
     * Returns the total number of source symbols into which is divided the source block being decoded.
     * 
     * @return the total number of source symbols into which is divided the source block being decoded
     */
    public int numberOfSourceSymbols();

    /**
     * Returns {@code true} if, and only if, this decoder contains the source symbol with the provided encoding symbol
     * identifier.
     * <p>
     * If we have
     * <ul>
     * <li>{@code K} as the number of source symbols into which is divided the source block being decoded,
     * </ul>
     * then the encoding symbol identifier ({@code ESI}) is only valid if {@code (ESI >= 0 && ESI < K)}.
     * <p>
     * 
     * @param esi
     *            An encoding symbol identifier for a specific source symbol
     * @return {@code true} if, and only if, this decoder contains the specified source symbol
     * @exception IllegalArgumentException
     *                If the provided encoding symbol identifier does not represent a valid source symbol
     * @see #numberOfSourceSymbols()
     */
    public boolean containsSourceSymbol(int esi);

    /**
     * Returns {@code true} if, and only if, this decoder contains the repair symbol with the provided encoding symbol
     * identifier.
     * <p>
     * If we have
     * <ul>
     * <li>{@code K} as the number of source symbols into which is divided the source block being decoded,
     * <li>{@code MAX_ESI} as the {@linkplain ParameterChecker#maxEncodingSymbolID() maximum value for the encoding
     * symbol identifier},
     * </ul>
     * then the encoding symbol identifier ({@code ESI}) is only valid if {@code (ESI >= K && ESI <= MAX_ESI)}.
     * <p>
     * 
     * @param esi
     *            An encoding symbol identifier for a specific repair symbol
     * @return {@code true} if, and only if, this decoder contains the specified repair symbol
     * @exception IllegalArgumentException
     *                If the provided encoding symbol identifier does not represent a valid repair symbol
     * @see #numberOfSourceSymbols()
     */
    public boolean containsRepairSymbol(int esi);

    /**
     * Returns {@code true} if, and only if, the source block being decoded is fully decoded. A source block is
     * considered fully decoded when it contains all of its source symbols.
     * 
     * @return {@code true} if, and only if, the source block being decoded is fully decoded
     * @see #containsSourceSymbol(int)
     */
    public boolean isSourceBlockDecoded();

    /**
     * Returns a set of integers containing the encoding symbol identifiers of the missing source symbols from the
     * source block being decoded. The returned set has an iteration ordering of ascending encoding symbol identifiers.
     * 
     * @return a set of encoding symbol identifiers of missing source symbols
     */
    public Set<Integer> missingSourceSymbols();

    /**
     * Returns a set of integers containing the encoding symbol identifiers of the available repair symbols for
     * decoding. If the source block is already decoded, then an immutable empty set is returned instead.
     * <p>
     * The returned set iteration follows the order by which repair symbols have been received.
     * 
     * @return a set of encoding symbol identifiers of available repair symbols, or an immutable empty set if the source
     *         block is already decoded
     */
    public Set<Integer> availableRepairSymbols();

    /**
     * Receives an encoded packet containing encoding symbols for the source block being decoded. If enough symbols
     * (source and repair) are available, then a decoding operation takes place.
     * <p>
     * The result of this method invocation is a {@link SourceBlockState} value:
     * <ul>
     * <li>{@code INCOMPLETE}: means that not enough encoding symbols are available for a decoding operation.
     * <li>{@code DECODED}: means that a decoding operation took place and succeeded in decoding the source block.
     * <li>{@code DECODING_FAILUE}: means that a decoding operation took place but failed in decoding the source block;
     * additional encoding symbols are required for a successful decoding.
     * </ul>
     * 
     * @param packet
     *            An encoding packet containing encoding symbols associated to the source block being decoded
     * @return a {@code SourceBlockState} value indicating the result of the method invocation (see method description)
     * @exception IllegalArgumentException
     *                If {@code packet.sourceBlockNumber() != this.sourceBlockNumber()}
     */
    public SourceBlockState putEncodingPacket(EncodingPacket packet);
}