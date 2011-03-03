/*
 * Copyright (C) 2011 Emmanuel Tourdot
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * $Id$
 */
package org.trancecode.http;

import java.io.InputStream;
import java.util.List;
import net.iharder.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author Emmanuel Tourdot
 */
public class BodypartResponseParserTest
{
    @Test
    public void testParseBodypart() throws Exception
    {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("singlepart.txt");
        final BodypartResponseParser parser = new BodypartResponseParser(stream, null, null, "text/plain", "utf-8");
        final BodypartResponseParser.BodypartEntity part = parser.parseBodypart(true);
        final String content = EntityUtils.toString(part.getEntity());
        assertEquals(content.length(), 218);
    }

    @Test
    public void testParseBodypart2() throws Exception
    {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("singlepartnoheader.txt");
        final BodypartResponseParser parser = new BodypartResponseParser(stream, null, null, "text/html", "utf-8");
        final BodypartResponseParser.BodypartEntity part = parser.parseBodypart(false);
        final String content = EntityUtils.toString(part.getEntity());
        assertEquals(content.length(), 115);
    }

    @Test
    public void testParse() throws Exception
    {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("fixed-multipart");
        final BodypartResponseParser parser = new BodypartResponseParser(stream, "=-=-=-=-=", null, "multipart/mixed", "utf-8");
        List<BodypartResponseParser.BodypartEntity> entities = parser.parseMultipart();
        assertEquals(entities.size(), 2);
        assertEquals(entities.get(0).getHeaderGroup().getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue(), "text/html");
        assertEquals(entities.get(0).getHeaderGroup().getFirstHeader(HttpHeaders.CONTENT_LENGTH).getValue(), "206");
        assertEquals(entities.get(0).getEntity().getContentLength(), 206);
        assertEquals(entities.get(1).getHeaderGroup().getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue(), "image/png");
        assertEquals(entities.get(1).getHeaderGroup().getFirstHeader(HttpHeaders.CONTENT_LENGTH).getValue(), "12791");
        String b64 = "iVBORw0KGgoAAAANSUhEUgAAAPwAAABJCAYAAAAZrEuhAAAAAXNSR0IArs4c6QAAAAZiS0dEAP8A\n" +
                "/wD/oL2nkwAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9cKDw4nLDO/fUwAAAAZdEVYdENv\n" +
                "bW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAAgAElEQVR42q19e5CfVZnm837d6U5fknQ63bl1\n" +
                "ujsJCJob4SKiEtIhCK46g2wNJSVMyR+iyRiGUudS5WjhrLU7VWMN7uyOozNa7DojNQoqiLqK7kgS\n" +
                "5JqAhACBEEjnBkk6CaET0rn2u3/073LOe573fD9qtqtS6f5dvu985/Jenue9iE6bpgAAkco/AKj8\n" +
                "Xgiglf/Dz0CAovK58Hd2jfDv6LNF5ZqY+F0q1wfq7xWo36/6XiHBtey4zHNUrwv7HhtncN/kXsHv\n" +
                "4RirY7bPCwmep/rMRfpaMreVz0Vza983zxDeyz6PXYNwLoXMSXItsi616Zd0LAJAq98P5j7cHxCy\n" +
                "5s59o9fJc4Tzw+ZZyF4rzBoLmY/adc39YZ/bnIlofPD3GoK/i+Da0TNIfa6jNa08X2HOCnsOsgeK\n" +
                "5CAj2OS1gQUvIX47OlDV/zW8npprVT+rwYSpuUH4JzlA0biEfKfyh1bGEn2OPUj18xq/FX5Xw/uQ\n" +
                "aybjEHMLjf5D7ZHVjEfj76qa+8DMk/m+HVM4j+EzVO8TjVudedJ0LWHerl2jiN8XM67oMNp9VP2s\n" +
                "xp+1G0Pt/KCunKK9JPX9Jemw6XzCPCrZ3hPXUiPkzESE467uQ3uvgs2BmEkVM49mINH+QLyHo3M4\n" +
                "8fmifjGNH0IkGL8VBMGBSg5B+PAKOtPCXlNHipvDVxUUVECwjaRGwxPBEI5LwDd19TrsHFSfNRqz\n" +
                "mk0sgdUC/l6yuchBsZ8HXxr+hzrCXOI1DYVUpGmdm0Z7VLmSUPg/Gm5OxAea7RdrtSEUjPZgaeXe\n" +
                "RgGFf1rtbZ83FNLRw1WuXbD9o+k+VLsnHSVnFYbYs6aBPGDzIkZ/1NekqB/cwmgAe3iNxFVjjlkN\n" +
                "lmg45DW4FSDQ+F7ZTRZ8V6VEXGs6ecxKAbFerJKzz6CaLqadQztOsUJMyLiVbCLjKql74o2FRDSs\n" +
                "1RJWMItwYW21tTXRIamLYS28ZI4coSv2cDOBqkR7G6un+nd4YNQoLCh5ZkdhVK0/sVYTkcI1S5Fo\n" +
                "exVuVahdC2evWkElxjKo3LdI9pjdzUpEnYS+ZXDorGkuxvyip18dzW0OGjPJROIDI4EGZRrC2wCJ\n" +
                "yS2pySxkI9kNGpl3xqRLfOtQOhep6UY1nKetrWnvSP7wcKp5X5VsqBKNbk3IRGibcQlSISVm04uS\n" +
                "NQrvFRwoRV5LKhO8SK0xeCa8pNo1UUAaKJGCW53RHtFUYISHlAkaKxhq72lq2dpnDO8LRUF9R7rR\n" +
                "y2y0jISJ/hHAKLII1CxwzhY0+IMilqCas2wdjSBkY1qtZudL7Wc11Sh2IKH5Gj1zOAfiuy52PGWb\n" +
                "H0xzMQ0pvkBNZI0QfIO4Dur4/sIsKmvlWSGmmQMhGd9byXypMd8l7y4y8Dfy0cjNEwFrNbxyiwwZ\n" +
                "jErAsaVEslp3RgKRZE0zOMCKOGa51e6q5qaZhYy+RybW+pviaLoaQCPc7FE4JiMREOkLZPNl/Ftm\n" +
                "7bgbWxzghg8j1ebO5gfZbOps4sQMJgfJzqsF18SsgxVuTIMywR+6WUrm3V1/5u+X+cvqCyDr73vG\n" +
                "qWSUUeK22X0uRLCx/eR7a8l+gOPqiFZM+qywkdgPiDaP8E0aSbyCoNrE3KKml+POKtOIBHgKEdpE\n" +
                "Q4hz8BwGgAmMSCtm/E8g/2z5F8g8EYEmjgCWwIy0ez/CHKpgkBpLQNIDUvaMViDY66gnUCW1mgqP\n" +
                "FcgBsR4mYYFQ4iYZjZhVeqKOgGRCzQCTYiw5T1MLuCtKBYKk82KUU+HzK44/GEkF9YGViHJrZFMj\n" +
                "Axyx6xpzO5LUSgSTEGFE/CxR3xUQzxLIUGZi50oyppvDGAj4ARRPojNfCulBpdQpQbkF3Pf3GBeQ\n" +
                "MUiwLnQTE1fBYwKFAIZ2jZVMkmb2rR2nGAbB27ta4vYquPBgOIoQrEglczbFv5byjxYpmEUCUdTQ\n" +
                "dSF4V+aDWEQr8e0J0h8Fhzgml+Y2O9J7qfoTy/xZcXzuxA9Wfpgi/zXUXEqoNg+dNb6eBuafIiM8\n" +
                "gFLzgiHvAv/AqGRMfiWgJvGfE7CS0FfqofMGY7H4jiBvfkdKgQSneLiDCMeyqKIRbmklGI03F2Yd\n" +
                "PNaHan/mnqS4VeEuYGg6J1LYATokAEPoAWGiRw3amIl6UMKjhgKg9rWCHCTh/rwXfKEeWipc0Cga\n" +
                "+Lz4pqaQjaBqfD2P1szJWubPEs2dTHvgDlnNaBFwBvAwvzvayJ4blcF3QvpWhGASmlpAUgIQhu4f\n" +
                "lMcmlMUiqKOdhbkHygUFJI8BiLeXM1YOoVaLOPJLUgmW80FVOLVW3aySOSAgSK+UiGkKTilhB9Sn\n" +
                "X0AoFQ9Yk3egRZkm8OhENQg45dGtO+BZBJoKYNd7YpF3jL4DwUMy4BE9VJ6ARSaQxgFb2bU9Lc5i\n" +
                "CiTjTkbUsaEKhQBq0fNonVW1DIEKx6mkCLadOGOTVElqhvpkwJ4V7ghNejfE0Aa/WJpKfVQajmkW\n" +
                "+uQK37RLQkbV31hJyK7wwxqGWIoD2lgqJRsC6gF77PksX6/mEBBzPgGaPP9c/U2gGQRfHLeLjUuM\n" +
                "a8QEhJYh4MoPnLsHLN+uMQAWWg+aoerg5Qo42JXCCUkmroVd8+R9TYUEHQMDVFmouueyZMDUQCEU\n" +
                "HJUX7utoDmSTVCioQ/+waKaEnvC4ZkIJeSauZLQ1je0mJpr9nak2lzIUo31zYb6a18qq4NFpGTQ6\n" +
                "ck9LQjCTfAWDICsy2jlUEEpMdRuv7whFz78Xzx8XYjGRdVVzPca22P3gRkLCRJkKCXARfp4SJaNm\n" +
                "7MjTuDl3LQlkEvp+QQeZ7nKDuivnji3CWZPESoSpxBMvFLUih00ylJABmVSI5S4NgFIZdJjSdZKh\n" +
                "YkAis9j3CqQx93ZTEpYhXKvE79R004rBTpKDQHhdJlxFMrSQAchEHbPdXEuFA4DJwVTfh41MajS4\n" +
                "LmzrMR9bCRDpnMokdNk5sCIZgey5jjk6OxMcV8uWSwI9hEdMiUN1qF1U9aV2SJmFVI3VruH9LCgi\n" +
                "JDCHAVOihBHIAGWRucViwhneQbRCjoEUks6qkgmeICY6NE75tCazOJaCZjLpkmxER0MnWs4xL5UB\n" +
                "rg69pHa9iUCVHJhF3CIBARfVAcIcrCrUzomFU7LWFMR1/MNq/AMFFj2OX8oZGOLaFtRsZqCbWpNU\n" +
                "g9h1LUerksg0m1mWieVOQC8Tfkv9feGbB8SkCmlGZTwqeNKMOCAOPdxCaB/JuxssUk0MbcSAMHXQ\n" +
                "8ySPiFGZxgdXh7pjAorlhruuRsbFY3HlmllGKXFpqDLKJLiEykeJ4qK59Jom4Xgub5LFZhJrvLOU\n" +
                "JA+JyXQNWS9OhxdUqtU0iOQRWBUuVgU85C/xzzQFn7wVFWehE38Lfoonc1kkZ06xIh4eH4u06IAy\n" +
                "05h81qdBMgCo3YDqa71kDZSYujmaTfIH142J1xSrYIFMDMNR5SZ5dVOrmPRkcdgIR/WLFzarKfNj\n" +
                "0XFVQl9nTG0Wk5BgYpYxUsMYWFfbUIqaEWIBGFW44ElaqSFF0EvBBMZHWUBLSmgxTwCQNE01CSzi\n" +
                "JHeEm57mFZNYgQQxzaC8rsUgzkKH91GfGmXFD1y6STko5y2cqG+OZiP6mLUTasnMGF3OGiQsGoTO\n" +
                "ItRiYgVKnjpNLBsi4FmINku2EaPHEkCRMFUAz2YU4QVQQhrQWmCqPmiNqoYv2HpkpKWCZ2Z5fGdS\n" +
                "QsmazJphCcp8F0vHWds748OzzewhnCpuLQ+uqXLYYiZEkvmHtFqLk3LsxdNTM9eJdaeKgnDbquCl\n" +
                "YByh5ybAqBNHbpHnIi+YaektK7xZNRyX50qVXq4ASDZIpmQPMFPeGzbN+Sc4FAU9NQi8AQk6EGdN\n" +
                "hZUSAimhFAoJhno6k67KAYiEUtI0O861CnKUIjKugzgeB8vTNnSTOPSllPnpatgG6y5pA6YrCBio\n" +
                "aQKTZ2FpDiAigk0dBDrZlKyoiXIQEeBJP14UnOW6tRE8iEk5yVihPhyURg9mBJmUCEma8AU/zgDw\n" +
                "MbC4xBXbSMRvcUPGwG8m4iP1qkTaakbzEk40jDFW8JhkxjkzpFm4G+W6IUwzK9LIKmXJMEw4qWNi\n" +
                "Cw/7ZOOgJZsY7kJMWxa2KQ7dKIaassU9WA6+585IzpWTFKPIRCzHFK+YmgeS8atZ/QTlgTVM2yoL\n" +
                "kHIsU2vBiRMfD5TXUEwCozRj/dXPSnOMkEqJVlRTpFDSg5yYXwHXWGT4VLWpkI6JI4WDtptEiFx8\n" +
                "smhEUNTe+r+/qf3+rUWLsGZggACXCvn5L+qfW7YMaxYsIGiwkPDGgmvkItxoheOTC8UcY+TeAzdJ\n" +
                "JlWRo9g0vV/BNhtRbypE64l5PpaEo6QCDrNIclSUOMJE09BtEcitt9bX8dZbsWb16nwefnIfcV08\n" +
                "uf76+rXX/gnWfPQjqbRT4gpoCdYCB6Bm50dS5qrZ9Y1pkEWRbt7KoGXnzvoD9vZizbRphHIRNw30\n" +
                "24cPY+3rr9fXdvlyJ/lBg8kKxsE4debHMP/WNb+cQo85njwxOUuS4KOUUZZ7TkqbsnJbpi7AH/3y\n" +
                "V/jxa68BAGa3t+ONT3+6BAuZeOlHL7+MGx98sPZSb1sbDtxxRyZMvv7iA9u24YYf/rD23Z/fcgs+\n" +
                "+u4LjTVm6V7JaECyyZOyZkVmLYJD1dD6gVuFFKQtAuVSlF/PK29WeU2uvbZ+fu64A2v+8A8NwBxa\n" +
                "OYUDqAr89PWJ9wqaHsgi2yJ0nlA7uR+Xpm9wEZgmcY2RDM+aTa/VPLii5efcnXTRzCNmLKosteaY\n" +
                "oJX3Vs2bV/v4/hMn8NKRIxmhJjXB8/Du3dG7I2NjeOHQIR/MDe758PBw7d0mEawYnJ8i6daSFE1T\n" +
                "pN1UYwfk8nKukoOrDRzM0KVSH79IElQcF4DuH5uZ1+i4hGNAUtXu6qxtfd5i0I754MImWY3JriWI\n" +
                "uqGnkqICyPs+iTnulVVmddbVD48tdWOYm+CshjixCG413MxzlmISbEPHzzA0d250i/X79hG0Po17\n" +
                "X79nT/KE63ftzgNfldfWBwf+0rlzMbWt1SgLqzyUP7cy9F0DlxANpCwTGplae4SB8oKHBH4wUPh6\n" +
                "4WwTG/SUs7ZYcQsv4aeWRl3wxJwAqyqyRSSFsVxK6m8X+cNCQyA1H0kFZOiaEiowaYxhTTzE2heO\n" +
                "S5PkLGuJRe+Baw3UZqPn1otWc1JAg/iDxTN6MLOtrX5o9+4lAGmsqUZOnMCLhw8DAGZMnlz/7u5d\n" +
                "eWRbgCMnTmDrgQO1j6xauNAVRpHWT4KoQNw/Tcs4J0hazoIrrStmiqFq+ect4BcJrAaYIUFjAosy\n" +
                "YATUFAJIk4Nc8BJGwuvQscgjKYkDlzLTt0FLN6wSal0CtnlCcWsK+fEKoiRFMkpocUJ1JRfzqZze\n" +
                "SmLybWmmAjziK1PCO4r4qmj5vr7a7xv27UMaems0eaDdb1m8GLPa2ye+u3s3VJVryMrvG3btimZw\n" +
                "1YIFvi/rglQeDcrSWkmeg53rsuyyhjwsdRFvjviTvSTMpfE0NwOYLWtjQXKHbSFUduF35MiEuWqG\n" +
                "QgChZbxGBsjl+JLIoaRSihrTSzMCA37EGUsESfjPTKpmtja9lpjkcPLGCx4Bqc5GJM871Bf78duO\n" +
                "HDYHLxYc63fXD/zQ4ABW9PcDAA6NjeH5QyMxWGX8yNCcn1QUuHJwkPifGWPMgpjUdAdx4dRfBxhB\n" +
                "LZpNtqQNK1iat1dVNhtsRfJQxFN8mYhUm6qsmdJlxJVsbsjkoFlqGVDJrmhtnzjgWxlYl6Dv8Tif\n" +
                "ffttfP/QCDaOjuLVkyfx1tmzaCkK9Le24v1Tp+LmmbPwoe5up3+X+BVNpIHyVK4fFm+Ot8+dwfeH\n" +
                "d+HBffuw5ehRHDx5EgAwc/JkLO/uxvX9/bhl4UK0TZqURrmpV6Y7H0O+al5frMH37sV7eno4eqzA\n" +
                "+j27ay9f1T+AvceO40cvvzzx3eHdWDpzFqEapfJ+/cC/t68PHa2tbkXet8+cxvd//wwefHEbtrz+\n" +
                "Og4ePz4xF52dWN7Xh+uXLsUtl12GtpaW1GQ1ZrSs/ZM6uv3JT2LN0BDOnD2He556Evc9vRnP7t6L\n" +
                "kePHcObcOdx5/fX46sc/zj1Q6506RVBe2LMH3/71r/HrLVuw9/BhTG5pQX9PDz5y6aW47bprsWDW\n" +
                "bGTLVTHcJ1sY1ApA+L0FrHKz9CuiA8/MRUJHuJutBJrXktraDSHUMZ9/4PQZrB3eifuPvJl8e2x8\n" +
                "HNvHxrB9bAzfO3AAq7q68K+LFqGv6psKMpFQHniS+U6NqzbZUBA8uG8fPrNpEw6cOpV8bd/YGPbt\n" +
                "24df7NuHr27Zgu984AP4SH9/UDiUVAnKhvHWN8u7u7sxu70d+0+cqB34tcuX02ccGav770t7e9Hd\n" +
                "1oaVAwORH3/7+y6nfC/33zn28OC2bfjMj3+CA5VDHs3F6Cj2jY7iF9u24au/+hW+c9NN+MjixSUn\n" +
                "M77HqwcP4oZvfQtbqyAlGnPH6xaUr8z+5v6f4M5778WZc+dqr504fRpHjh/HluFh/P3PfoZ/+tzn\n" +
                "cMuqVc5edkBKr9oUbaQKIgRtUg2J76j8UZQ2JqzesCCmvWjJgbVRfMrNlEbi8s1Ht4+N4bLnn48O\n" +
                "+yQRXNTRgaunTcNlnZ1oDZ7t4aNH8f6nn8bukycJGOeYnGoprwyFEn4+ePzvvvYaPv6730WHvae1\n" +
                "FStnzsTKmTPR09pae/31sTH8wb//O763Y0cKjDJAkG1W81ypH0/WWDXy31f2DwACLOntxYwK8Ldx\n" +
                "927ouNJ2Wq7/bqjd727ahI9/71+iw97T3o6VCxdi5cKF6OnoqM/F6Cj+4DvfwfeefNKxGlOFM3Ls\n" +
                "OK6+667aYZ8zbRquPP98XHXBBejp7CzxJcI9neYZ/PV99+FL//Zv0WEf6OnBykWLcfGCBWgqCpw4\n" +
                "fRp//I1v4BebNzemQNwWahmF61nRYbUh2pV44v/m1IQgg1Kv0Z6HIIJINFuoUlEa2UexAMHRs2fx\n" +
                "4Zdfxt7TpwEA3c3N+K8DA/jjmbPQMam5tuFPjI/jH/btw1eGh3FaFXtOncIfbd2Kx9/7XjRJGfgG\n" +
                "J/1Wst5L+MuTh49g7dNP16Z+RksLvvney3DjwCCKYgI+GYfg3uFhrNu0CYdPncI4gNsefRSLu6bj\n" +
                "spm9eXApwiFIzbxCsKq/Hz945ZUJi6iCwi/q6Um6plbNeQAYqmh2EcGV8+bhp6+8gkNjY9g6MoJl\n" +
                "s2ZWWjPXly4051uamvCBwYEEB3ly9x6sfeCB+ly0t+ObN9yAGy+6CEXTBC4wrop7t2zBuh//GIdP\n" +
                "nMC4Km77wQ+wuG8uLhsYzHezAfC3Dz2E46dOYWlfH/7h5ptx1QUX1tZwXBX7R0dLAGNOvT7y0jb8\n" +
                "9X331f5eOGsWvrN2La5etqymuA68dRRfvPt/4Z4NG3Drf/+Gv5mlAbND0YBbQPZBaDGobRTDGMOk\n" +
                "gJ5TSFHFR0Op2jPZSxEFk+kflwQATXz3L/fswc6KxpzX0oKnly3Dmjlz0NHcFAF77U0F/mJgAA8s\n" +
                "WVJ70E3HjuFf3niDxByUHOQy7yUxtQTrnnkGZyvg55TmZmxYvRqfGJyPQoraPQsBblq4AA9fdy06\n" +
                "mieE1RlVrHvicXKwxWc6HJkdaviInjOWTRWwEwBXDcyrvbVyMDTrh2nWY3jgr5g3D22TWhLga92D\n" +
                "D+Ls+PjEXLS2YsOaNfjE8uUTgq+aulkUuOmSS/Dw59aho+K/nxkfx7p77+OWoVmT46dOYfHcuXjk\n" +
                "L/8CV11wQbQwRSGYO72r3K7X1KW9/e67a1ea3dWFDV/7Gq5euiyqVTirqwvf/8IX8Kmrr8ah0WN5\n" +
                "IV1WRBOZDkm2O496IDRjRdQEd9sQT8Z1El/R/qwdGYFs3w55+eWJfy9tg7z44sS/F16EPP8CZOvz\n" +
                "kOe3Qp7bCnnuOay1PhetkyfYe+oU7j54sPax/33++ZgfcMZJRJYA/6lnBm6dPbv2iX+sbfqSCCfN\n" +
                "ILEeilr5efTQIWx+s+5u3LlkCRZ3dcWMQ3BSl3Z14yvLltXefvLQITxefc4k7bfEHAzW54LubswN\n" +
                "TOX1e/YmsQYh/76opwc97R21768cGAwO9q5Eqhw5MUb895guenR4GJuD9b3zmmuweM6cdINWfpb2\n" +
                "zcVXrquHmj65axceD0K3c/EQ37r5Zkxr63CCksT0H/Q0cP29x7dvx5Zd9TiEv7n5ZsybMSPWosG1\n" +
                "//622zBjypRGeOZ3CFiThS7gZLeK0ymXNaLQTDXPqFKMljZ2/Q/9qFmoiqb/weHDOFv5yMXt7Vjd\n" +
                "1UUKtRjwTIFPzZlTu/TmY8cwcvp0Y5OeDccNXjQAzP376gervakJnz3/PNDuuqGgfPeFaGtqqv19\n" +
                "f01zkuw4Ff89E2YVhtlu2LsnsRxC/32ofyB63uWzZ2NaBWfYuHs3FOPRrTbsjv33oQUL6oZjRaDe\n" +
                "/8IL9bmYNAmfveKKNMzbPMfaK1dMMBbVudiypdTkfffs2VhxwQVBMonwZhuas+hiCu2BTZtqb3e1\n" +
                "t+OTK1b4ue8KTOvoxM1DQyXWaokTLyUYeFVRqSn+GVGb/GIF5a415z+EdbiU+haLWlpwXXv7xL+O\n" +
                "jvRfZ2fwbwqumzIFiwLwKufPP1zzw4APhYcd4BqgcggvqoE2Ez/P1YAjKUfsJedDcX79iYrGBICh\n" +
                "mTPROanFqV1X/3tqSwuGAkvkiZGRzL00DegQDpoO9dcP/MGxMbx4+EhU0Tfk30MTHgIUIvhgRWAc\n" +
                "HhvD1oMj0ZjXB5p3cnMz3j/Qn1gjT4QC5bzz0BlRdvzwTZ08GUPnn1+fi+Hh0kSmFbXPa75SmJcK\n" +
                "DkP/CvDUjh31sS9ejJZJk3jEX/AYH7700sxeytVjLPPtcyCdOFV74jPRnEvxS2ua27RW1hoKuL1r\n" +
                "OtZM76rumHqGTzKguib+9uHDWLt3b577B/DC2FjtIz97801sqVBOeVMpfe31iCJzOpkKMzklYwnU\n" +
                "te+OAIle1tXlBEdoktywrLsbv6yYvztGR3mbL5YRaDv1hqj5vP7o74f37Mai3p7aV0PAbmV/fyIA\n" +
                "Vw4O4v+8+urEd4eHsWz2LMq/v7+/H61Nk5JAqx2B8FtWFWheTn7w1WVz+/DLbdsm5mJkpK61nWSY\n" +
                "hb29MTRlfVkvCtwFQoFX9u+vvb24EoiUNkKJGaklodD00PcwpbmQciXjRhhmrDubfqvCeHjwKEKa\n" +
                "51xCbhZOrrtXNDKD+VV/Dpw5U/t929gYtgUC4J38jI2fK0nAIESClPH19ck9UmEQAKB3ciuiFMeE\n" +
                "raiXnO4N8IjDVaEkjt9eM1slM2bBeV3T0d/ZiT0VIbR+zx587uJLAEHkv79nxgzM7OxIBNPKwcEI\n" +
                "oLvjfVcABfHfq3UBzM+RQCj3dnYGVZHDMs1p7kXvlLpVdvjtt1OWyFiiU9va/OjKJI5BMhx8XeC/\n" +
                "GQjuGVOm+qGsQdxF95SpxDKTtKEK605j11EJntVYIItJNpOYmk87uRppQiuVZkDGqGPrO0hLTF6P\n" +
                "TdfT+v8HOBhXlCO2og0MkqN8Z4NxthRNJC6BI7KtRX1Jzo6PN5iW6/QB0LpPOtTfH/jxe2vrGfnv\n" +
                "AwNUsF86Zw46K/70xl27oJUcgQ27h2P+feFC2oW3is5XabvU6uLho63NzfFc0AaSoX4hhUfUyT9A\n" +
                "WVPPiZ/TZ8/Wxz6piRclNdujNcAeUsWmJQUuCCsjpLy2W2SE/C420i4pLMloAzEFGSRfcNJrM+Sh\n" +
                "zB4zYRoutBcFjlc20F3z5+PzfX1peeia5ijSogO2RryL2BpEHI0cvPoHO5qbcbyyWY6dOZNvhRVI\n" +
                "79GzdQumsxpmSzvwZsJ+bUilAKv6+/GvFfO4muO+uLc3DripRdbFCHFzU4H3z5uH3+zciSMnT+K5\n" +
                "Awdw0dw5WL9zOALjLp83j7bm6mhpwfGKxXPs1Gn4TU00Mm9HK+HHACb8/mgfvoNMSrHFMjKJWyqR\n" +
                "VzC1vR1HKlr+2NhJv6BooFWPeVZnZNVIeSmEhjPqQKID+YX93nIiTpqnZgA9duhJa6UIaZb8A5mH\n" +
                "7g/AvdC8T+OR80Ao7yzBADJWwMF59sB86gtSU189fpwnV2gqlV8NONy+jnbirylKK64SiT8UIPVV\n" +
                "P34iYYb478RqXDk4v/7d4WFA44SZDw4MoKWpGWmikKJvat3EffXwodiUtcE0QWLWq7XCG0DftGkk\n" +
                "y9FbC9JxNarPqJke8vGemFWt3ARg+ODBFP2O4kQmpMTO/QcyJratoTjxh/72t9DfPgxdvx5rgvJY\n" +
                "1HJzhY2z/wMBU9Dyv2oORBgcI0Qql/JzTjM91h4pkbaxr3dZwCk/fuwYB0TCxdMGTHJK82imZ3mO\n" +
                "Qpz4/NJgozx26BCP1pN0kz0WxBgsnd6NfNtkA6i4ZbeBBV1dGAz44fV79mLk7bGa/35hdzdmV5kM\n" +
                "8qzWj6fx88L95qVz6szDY7t20Xx6Btw8FjAAS/v6nB6ITIZLSXdWry1YGip+cZDmu2nHDlK9GcH5\n" +
                "mPj+ple2l58HcepEoKR5i5RV+ZEsFl3QrqWJuUV6XUclmTP+EDJ906mvzBanTgVeP3167SO/Gx3F\n" +
                "y2NjaXeOXP1w1jGFFuQV3viyzPyqmNLXzJ5VBxdHR/H0kcN+qbHKi5sPHcJLb71V+941JkoupmTg\n" +
                "siReOnLox2/cs2dCy4f+eya/4fJ5fWir+NQbd+3Cw7uM/75gIbxyYtec/676XBw8iKf37QFvYVZ3\n" +
                "vTbv3oOXAoFyzbsvBO2Bx4Sh17U4FM5lCVsVhPuqRYtq7z6zcyd2vLEfacBQbHH9YONGX7tTQQ3i\n" +
                "lsKJL8kILGapGEs9dpA111LIJTT9QAH1BIh1E6QEpawfvutnzMD8ilk/DuC2HTtw5pxysCPiSTMp\n" +
                "hS5mQvqf5fC64Ozd2N+P9iCI5s+eeaZSRILNr0AB/HkQ5NHZ3Iwba9rFqxiTs+XSDbcqyH4bGRvD\n" +
                "N595hvjvHF1uaWrC+yoC6M2TJ2yUXesAABFGSURBVPE/nniiPtaWFlzWN5c0r5iYlBsvWob2AMj6\n" +
                "s5/9fCIRJ9ljE59XHcefP3B/5L/feMklRtM52Zqiqcf2Tn+CGIVPfOCDmByM/a/uuQduKXIAv/n9\n" +
                "77F+69ZSUDcF8AC3EUXSshtxHQCVPNWpYTNJIS2hvW4mrFsrHJrA9mRTj8tAPtvOmOXNIvifgZn1\n" +
                "yOgoPvzC89h76jS5dNjoT7Fz7CS+tOMVfOGl7SXjdyiVMgoxeK+7dTI+f+GFdTP44EHcvmkzzo1r\n" +
                "IgDPjY9j3ROPY33A+X5x6VJ0tTo14ezcWYGqxE1Sxar+mI/fGMQ9DNVMdgKsVl5aOb/ux28Mwk1X\n" +
                "DA6iuSrc1GohRXd7Bz6/YkV9Ll59Fbc/8BOc07Ql+Lnxc1h334+wPgh4+eLq1egKwn3TXugM+8kU\n" +
                "n8z6vxJZa10dHVh73XW1j9372KP4bz+6j1oGW4d34eav/21jrq2hCWXVKsjQEGRoCN/+6U/Lqx1T\n" +
                "a0rh1lCsnMdmT4EkB6cwUjRCzzMVOxRAEW5AVmUgR4nZvF7Bx7q78bWBfnylEiH227fewnmbnsIN\n" +
                "PT24uqsL89va0NHUhNFz5/DG6dPYcvw4Nh49imcraOun5s4llVI9Ne/0IncXs/7ZLy9Zgof278fm\n" +
                "I0cAAN/cvh0bDx7EZ951AZZUApOef+tN/PP27dj65tHald7X24svLV/umK6mhVeUEWUAPRN4MjB1\n" +
                "KhZMm4adgdsAAO+aPh1zOjvSzqcaC5SV8weBDemTr1q4gJivsXvx5dWr8dD27dhcETLffPQxbHxt\n" +
                "Jz5zxRVYUim4+fz+/fjnxx7D1jfeqM/F/Pn4Uu3AaaY/XOYn7MVG+hG4rlDl+f/LTTfhgaeews4K\n" +
                "vvJX99yDh559FreuvhoLZ8/GWyfG8JvfP4vv/vohnDxzBp9YsQI/fOSRzHywClJlTAOcoq4WD9LY\n" +
                "5Sjic9kcR/wwlJoxQdI4bSXm2mX93eJVAq0tLsCX+wfQ09yCz+98DSdVcVoVPxwZwQ9tOGoWOwlm\n" +
                "RDMBzBLWIS/ZKMGmnNzUhF8ODeFjGzbgyQo4tvXoUdy+6Sl3SB+cORMPXnttna8WpPX3XaEvmaq2\n" +
                "E+Na1d+fHPg6/+7VE5/wz67o60dLUxNOBznhCf8u1rqasC4mt0zCLz/9aXzs7rvxZIUZ2PrGG7j9\n" +
                "/vv9uVi4EA9+ds1EOGttL9lmKPAnhIUbe64AWCTexOc629rwmzvvxFVf/jJeryREbXzhBWwMcgRq\n" +
                "4OLgIP7pT/80PvBaYlWUpb0me7Yk9t6pDjThw6v41GS2KV6O1pK8W8k4/lKzOr31mrlzsO2yS/HZ\n" +
                "2bMxLfCX2c/kosDq7m7843veg7tqpnZ5B5G48H+JBLatrwXomTwZj1z7IXz9kosxJ6Dq7E9fezvu\n" +
                "uvxyrP/oR9E9eTIpr6XlbqESs9XErK8ivvrKgYGMu1Jfn7ZJzbjcAInTWltx8Zy5hI3RBD/p6ejA\n" +
                "I+vW4esf+xjm0IyyOgV313++AevvuAPdtcg/1u2mbEIa3J8J05O6aefNmYPf/93f4aYrr0RTkcbn\n" +
                "tjY349PXXovHv3EXpgVMUuTrexWhGdWtma7BbjdfgquZZCHRuXM0rgRbIM3qIqWWRNKQQUHquxfB\n" +
                "IApJD5Kwvyu/FxZxLNx7nRkHnn77OF58+wQOnD2Ncwp0NDVhZksrLuxsx5KOTkxubuax/IUQE8np\n" +
                "/W5LKBeZ/vBhsIdMgIybDh/Bc2++iYOVsNmZk9uwfEY3Lu3tnYgUs/MXjdMsrrAxI+3WG42DPF9y\n" +
                "XUkjL+18FDYOPMxOkwR1t+W6xlWxac9ePPdGpaadADOnTMXyvnm4dKAfRVMTGSvy+7Bw8CMgWCcy\n" +
                "f8n7wV60+wPA/qNH8dutW2s17eb19GLVRcswvXOKs5eK9DxFeSbeM5G5CxVLYeo5eOsWPLfo3LlK\n" +
                "DyfdwOxve+BNG5zkb4lbV2U3ljOO2qYzm7gQ50BUJ134pqfCx9sc9oCAt8QOBagNnpESIZmbAyFd\n" +
                "d5Oe45LOt31GkEMb3aPICH2JhXG0xpmNqiUHr0zIMYErnuBz9qqncOz37L6N9gd7xpwwFkcZsHVj\n" +
                "wkcyz1dwgeyc3YKaPUkbKOEIfBK0YktYO7m/KilAlNBgSpoySL7XOzKF/sOCE16gjUftMb+YTr5D\n" +
                "62mGW821+03SOEu690SH1nlGyZivEaNisnCExUFL/h7hRxV+a2q6t5xsTberDNLmk2A97eHMNUHw\n" +
                "3cYRCtoIM+vCOpWJtMxvzwE3ueAipElaapNn1JTP8Q5f0p5KeaGBHOsVVTtW0rxSOOChQBaxSHLE\n" +
                "JQ0OSDrfSHlfMmRouaRhgj0XGiO/IddLe5Rpygl7lKdkwB/Wg4xSRF5FVHX9+WiRlXQftoCRkLbV\n" +
                "SdAJYWmEHdoMc4HMoc/kqLgALAWnS8BAzc0p0jRnq9RU05gDyTS2zDIWcSp7kQQ+iJKFsFF2YT06\n" +
                "835ByH8Vp/iDBbqcA1yqGeDWQI/CLaPnKFIhYaOwVEnmFZFkImnVERZKm21/rPnefm6AiaDhckmM\n" +
                "jVB7b+HUUS13n1SuFa/3mZoAKJI45RXCyBVyTErjmyAeRQbwkrxxB5ZpB8fScbScODSawrEcSfEK\n" +
                "cBCYS6scMBlbz0WiUbJJ9Yb3VVZ6SdPrRIkS8IseCKEUmJVAg2EcC0CUx6+rxlRPOE4l+dOFZPq+\n" +
                "KReM1pSN6vNpcC8hzTez0gxuS+XErCPjZf6smoMaXUfTRXK1omS0IDFXNUzKIia3OM+s6ofZCnix\n" +
                "S5GMxrYIt1ff0YyzbB6So+QEqqlxpXIt3JLONdZNIJamaoCaqaS5xhBuMojZELY+uEg+coAeGm+j\n" +
                "m/uIl2qrcKvjJm2Jwft/izELQXjlRg4f6++lpIU1C5qwoZOS41RBrBHwuVbyvzv+TAi0rTSTCAvi\n" +
                "KuX2hBqTlVakDV9X8ErKcEqzaeZPJ/qO1hggGldK1l/UXxfmKgnS0nFW26uxMpSNUxzLIcBAeeaN\n" +
                "5tWwZDhyBr55HKOtBJIAKUKQVPJ52zuMJk40GpBDepgrSJlhv3ZYWh7LmZNkccJUS6K5PH5WnE49\n" +
                "VjB7ZrRNghIpATZDt8UcclUfALOVVUXSohuJGxoKGM2UFVeOd1AATtMDkz084BaIh5UocwHgjMWq\n" +
                "40xZM8kJaJJAoDELUCSorziN/Mq6b1KfjGl2Ia1xNPVhXQFBzHMxfrQ4WIGt0w31QS7PfJccWOiA\n" +
                "Ul7Glmv+qo96Ur8wc0AZtZnzFJDrL0hATrYvWL6FZnIWhFTsYXuJdsltoA6ZZSbU0FhwWnmF1V9D\n" +
                "66NoAKln0ksyxU+izzCXQTng7R10cH1dpCG0DXQ7Zb6ph+xn6QQnx14a2OTZeuya+tdCqBuWZKHI\n" +
                "0H0lAdxCTFsmQBiQpZoxozVbwYhK9Jw/nU3ThNMdKANGuVQXTBCLZqhf8hzq+cmkUUMuxNgpRcXn\n" +
                "QHx6lrl46qRZuyB1BtNI4jUacEESZgpZwV5wQEx9ie21XM7SPg644C6EEBxB81aEIg80RpaAWb1I\n" +
                "sytHsdliWfMyAeA0RZu9jWkXSplAFF40JCl5JoTabCQPIEO72rmmYKGSNWfdhzIqiJVcti6HgJdX\n" +
                "E4L9QAgnDVLIQlNKTTzLroSqFWd/0M+XtI6WHCWsPlBL3VsJSlxJGTClnHNMTJVcoX1N3QaKlgYU\n" +
                "oZhmFCoc/KBovUNtJZrKlt42AkDEmSPzGXH9m0xp4ZyLlEksInK5nLIU331h1Guq1riAjQSMGh8e\n" +
                "aKwAKOKuu+wA0JZKjOYsHPeE5ZAjFghMiNr6A2xtxKDtSWXiDAVHmoCmuRkBOJ3rRhNG6iXfR8jD\n" +
                "N8Ajpivj9mznvnEIgGXa7oj1hSUGbRLpRyLTvE6vlnP3KDwlppLC8f+FTk1UnUYNoi2e36u+RmmE\n" +
                "U86VgCo9c5pH7217YnGsAjoOJ4hFwJkadSwQEX99oyQVGyvCMAhzmEJ3Twm4mnPRlbmPHgCo+fp6\n" +
                "jHEQBwRkrIYN3AnwoKJ2oNysQSVSPDSHBeWRSNbEUkNVZSbSRlyppFJOlQBibBwGIFQvRFS4ecrQ\n" +
                "2GwGXcCDJddzQlBpzXV7WNQEPcGPZRDPfxdjvqIkPNloUFI5OSnskNBqHpcvJODL66qjqR8tZWHQ\n" +
                "cMqCW8uVWGSaw0KYlZPBoTRX36DB+3iCWUuiCyvrVqRN6IikzUU1uYJdMiALCb1UON1mrGbU1E1I\n" +
                "gDEbmuolthC0WQMVrUhNo0bq4ielhSTDPhArSRwwDU4FYCkB6Tw3QkLXyXENRHzhxmI3EpNUTaBM\n" +
                "Fun0WRPP1/dcFw+MdavhONiSBU1VMuHKBoxjTJc4Qo2mQEsDOQcwcSeIOzSbMOeCglL2wKnpEOIh\n" +
                "lqTqSxpnDUcKElPFazgYLUbhgIcSMw/qcPQKv/5eZAUoMc8seGbcEHFcFyEL7GXDWV+ONVsAa8Dg\n" +
                "cNkeFkHBW0NPiXPwpEEajB4Y5YeM5YqXgcLaQOm1rHYWQscRwDDHlGQFkaEUFfkmLaJk3VBedcm1\n" +
                "mrXiwyuD+UCy04SHvmd7lotPgYCEtTaKHbpmrINEi1NKqCjJjEq6flgrQ30zHvABLK9pZxm+oRk6\n" +
                "Sm2DT6/6i/gHyLMaQ78zS7kS9gUZ/A/wQVgl5nwuEIldT7znlwyX7VlDXoCQ5yawMVv8K1drn3Tk\n" +
                "EckDsonQjK3kIgLDRH0zS+Hzk4pMyKwSNDnXCMAcTCURTYn2sQIkU8HGsyI05wuSwBll2pdodmEA\n" +
                "oaZ9zBitFLINkkPuHcpUCDVFqxCTlmKM1hFPvWWi/OBQaAkWBIcK07RdlDgNJd35M8I0TDxKeicq\n" +
                "3K5KavIsXO5fMtmWjkUsWhK9irSRa87yga2ZX82WA/FTWb1zilBKSknAaYlkI5uUFTTwNHYDIbK0\n" +
                "zj0RTKxNE+DkBThCrECaq61IU1mlxMcOXY4cRWZ9UdaxRzIN+kTjxc9lH0YodybCUJGJxGRjUh9g\n" +
                "lJJ+S8m20pRGRa64iZe2TPYIrbQMfsgZ+OjVe2TBZU57b1bDMeumKHPFQYVOkVJdhEIBQX29Gngs\n" +
                "sIZllUmGJ2e16t3oIUlDIL0gj0RKws8ndyOllJv7QtD0hLs1CKgat0Iz2jpBxJVwzJ6Ars5n4fjB\n" +
                "DtctqUnogoKM2altbGZiM4nopU9bQJAdtIyr4rVTK6MoGd+duFzqnwEaIeo8c0TTNRinYdkj9Trx\n" +
                "1J+loBNcmvwCjnYzbpD2jzOFEyQTIijgSSMJ/ebQWOL87yGrjFZKIvI8f1UIpcfivA1zQPl9JSWK\n" +
                "tARdhonKy/S3z6L4pmtpUvIafI3YnKjk+ehsdJn6LRBY4BHznT2q2KvAYzU9u4+WJdnkMDWnpHum\n" +
                "wJRb0EVKknKkrter81n4xe2N+Z1oYEUaAUfMfc9Pd7WmYQcU3LRkWABjA2yFGXU2aYiYahmmpAQs\n" +
                "E24uRtgIuZZXxot2IZG0xnmSpZXxn0XhBgxZ01Et5VniroR+ZrgfvDic0BzNtjhTH6TL1WlnNJfm\n" +
                "cuQJ3akOFsKaQnJfxXcvvH6AdKO/gwCqJNQ7/mKRpJq6h5BpB3Xet5VzHNNGvBJANnZenUXKoKlW\n" +
                "itsIOKoxxKHTmF8qabWnpC5cg5QZkAmjLUPEHbOwdnDVVMcB930ZpZlsGlK8Qh3wTnKINzm04rgi\n" +
                "rAGpZLI6XUwJDi1bUnRDMqesqkSiRhdONqYSXAWOVYSSQjGaSwbSlO8PxlNEUkyRJpZ4ZrDkYsId\n" +
                "sCUZuCksqYT3pH3ViNSWjJmU9cu9lE/PVfFywq0pSEx7Wj6M+PylBTecTc5KIbNkFPF8fU3ntizF\n" +
                "U0y8Q+LLZxB4z9dmvq7NeAzpTnUsopyC8OoGROi9k4Ztt5qK7xq6BTqE13jwCmyIcf3UaPKcYAqu\n" +
                "/f8AMGN9ADsjEr4AAAAASUVORK5CYII=";
        final String test = Base64.encodeBytes(IOUtils.toByteArray(entities.get(1).getEntity().getContent()), Base64.DO_BREAK_LINES);
        assertEquals(test, b64);
    }
}
