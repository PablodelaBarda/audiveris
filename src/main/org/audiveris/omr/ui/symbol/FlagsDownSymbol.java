//------------------------------------------------------------------------------------------------//
//                                                                                                //
//                                 F l a g s D o w n S y m b o l                                  //
//                                                                                                //
//------------------------------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">
//
//  Copyright © Audiveris 2019. All rights reserved.
//
//  This program is free software: you can redistribute it and/or modify it under the terms of the
//  GNU Affero General Public License as published by the Free Software Foundation, either version
//  3 of the License, or (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
//  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//  See the GNU Affero General Public License for more details.
//
//  You should have received a copy of the GNU Affero General Public License along with this
//  program.  If not, see <http://www.gnu.org/licenses/>.
//------------------------------------------------------------------------------------------------//
// </editor-fold>
package org.audiveris.omr.ui.symbol;

import org.audiveris.omr.math.PointUtil;
import org.audiveris.omr.glyph.Shape;
import static org.audiveris.omr.ui.symbol.Alignment.*;

import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Class {@code FlagsDownSymbol} displays a pack of several flags down
 *
 * @author Hervé Bitteur
 */
public class FlagsDownSymbol
        extends ShapeSymbol
{

    /** The number of flags. */
    protected final int fn;

    /**
     * Creates a new FlagsDownSymbol object.
     *
     * @param flagCount the number of flags
     * @param isIcon    true for an icon
     * @param shape     the related shape
     */
    public FlagsDownSymbol (int flagCount,
                            boolean isIcon,
                            Shape shape)
    {
        super(isIcon, shape, false);
        this.fn = flagCount;
    }

    //------------//
    // createIcon //
    //------------//
    @Override
    protected ShapeSymbol createIcon ()
    {
        return new FlagsDownSymbol(fn, true, shape);
    }

    //-----------//
    // getParams //
    //-----------//
    @Override
    protected MyParams getParams (MusicFont font)
    {
        MyParams p = initParams(font);

        p.rect = new Rectangle2D.Double(
                0,
                0,
                p.rect2.getWidth(),
                (((fn / 2) + ((fn + 1) % 2)) * Math.abs(p.dy)) + ((fn % 2) * p.rect1.getHeight()));

        return p;
    }

    //------------//
    // initParams //
    //------------//
    /**
     * Specific initialization, based on provided font, depending on flag direction.
     *
     * @param font provided font
     * @return initialized params
     */
    protected MyParams initParams (MusicFont font)
    {
        MyParams p = new MyParams();

        p.flag1 = Symbols.SYMBOL_FLAG_1.layout(font);
        p.rect1 = p.flag1.getBounds();
        p.flag2 = Symbols.SYMBOL_FLAG_2.layout(font);
        p.rect2 = p.flag2.getBounds();
        p.dy = (int) Math.rint(p.rect2.getHeight() * 0.5);
        p.align = TOP_LEFT;

        return p;
    }

    //-------//
    // paint //
    //-------//
    @Override
    protected void paint (Graphics2D g,
                          Params params,
                          Point2D location,
                          Alignment alignment)
    {
        MyParams p = (MyParams) params;
        Point2D loc = alignment.translatedPoint(p.align, p.rect, location);

        // We draw from tail to head, double(s) then single if needed
        for (int i = 0; i < (fn / 2); i++) {
            MusicFont.paint(g, p.flag2, loc, p.align);
            PointUtil.add(loc, 0, p.dy);
        }

        if ((fn % 2) != 0) {
            MusicFont.paint(g, p.flag1, loc, p.align);
        }
    }

    //----------//
    // MyParams //
    //----------//
    protected static class MyParams
            extends BasicSymbol.Params
    {

        TextLayout flag1;

        Rectangle2D rect1;

        TextLayout flag2;

        Rectangle2D rect2;

        double dy;

        Alignment align;
    }
}
