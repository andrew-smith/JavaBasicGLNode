
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;



/**
 * A Shader Node
 * @author Andrew
 */
public class ShaderNode extends Node
{
    /** Default value for null shaders */
    private static final int NO_SHADER_ID = -1;

    /** Compiled vertex shader id */
    private int vertexShader = NO_SHADER_ID;
    /** Compiled frag shader id */
    private int fragmentShader = NO_SHADER_ID;

    /** The complete shader program */
    private int shaderProgram = NO_SHADER_ID;

    /** The source code for the vertex shader */
    private String vertexSource;
    /** The source code for the fragment shader */
    private String fragmentSource;

    /** True if shader is ready to use */
    private boolean shaderReady;


    /**
     * Creates a new ShaderNode
     * @param name the name of this node
     */
    public ShaderNode(String name)
    {
        super(name);

        shaderReady = false;
    }


    /**
     * Loads the shaders for this node
     * @param shaderType either GL_VERTEX_SHADER or GL_FRAGMENT_SHADER
     * @param shaderFile the filename to get the source from
     * @return true if shader was successfully loaded
     */
    public boolean loadShaderSource(int shaderType, String shaderFile)
    {
        boolean success = false;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(shaderFile));
            String data = "";
            String line = null;
            while ((line = br.readLine()) != null)
            {
                data += line;
            }
            
            if(shaderType == GL.GL_VERTEX_SHADER)
            {
                vertexSource = data;
            }
            else if(shaderType == GL.GL_FRAGMENT_SHADER)
            {
                fragmentSource = data;
            }

            br.close();
            success = true;
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ShaderNode.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        catch (IOException ex)
        {
            Logger.getLogger(ShaderNode.class.getName()).log(Level.SEVERE, null, ex);
        }


        return success;
    }

    /**
     * Inits this shader node
     * @param gl
     */
    @Override
    public void init(GL gl)
    {

        //create the shaders on the GPU
        vertexShader = gl.glCreateShader(GL.GL_VERTEX_SHADER);
        fragmentShader = gl.glCreateShader(GL.GL_FRAGMENT_SHADER);

        //send source of vertex and compile
        gl.glShaderSource(vertexShader, 1, new String[]{vertexSource}, null);
        gl.glCompileShader(vertexShader);

        //ensure vertex was compiled
        int[] status = new int[1];
        gl.glGetShaderiv(vertexShader, GL.GL_COMPILE_STATUS, status, 0);
        if(status[0] != 0) //then something went wrong
        {
            Logger.getLogger(ShaderNode.class.getName()).log(Level.SEVERE, "Error compiling VERTEX shader");
            return;
        }

        //send source of frag and compile
        gl.glShaderSource(fragmentShader, 1, new String[]{fragmentSource}, null);
        gl.glCompileShader(fragmentShader);

        //ensure frag was compiled
        gl.glGetShaderiv(vertexShader, GL.GL_COMPILE_STATUS, status, 0);
        if(status[0] != 0) //then something went wrong
        {
            Logger.getLogger(ShaderNode.class.getName()).log(Level.SEVERE, "Error compiling FRAGMENT shader");
            return;
        }

        //create the program
        shaderProgram = gl.glCreateProgram();

        //link shaders
        gl.glAttachShader(shaderProgram, vertexShader);
        gl.glAttachShader(shaderProgram, fragmentShader);
        gl.glLinkProgram(shaderProgram);

        //ensure shader was linked
        gl.glGetProgramiv(shaderProgram, GL.GL_LINK_STATUS, status, 0);
        if(status[0] != 0) //then something went wrong
        {
            Logger.getLogger(ShaderNode.class.getName()).log(Level.SEVERE, "Error linking shader program");
            return;
        }

        shaderReady = true;
        super.init(gl);
    }


    /**
     * Gets the shader program of this node.
     * @return The shader program ID
     */
    @Override
    public int getShaderProgram()
    {
        return shaderProgram;
    }

    /**
     * Turns on this shader
     * @param gl
     */
    public void turnOnShader(GL gl)
    {
        gl.glUseProgram(shaderProgram);
    }

    /**
     * Turns off this shader
     * @param gl
     */
    public void turnOffShader(GL gl)
    {
        if(parentNode != null)
            gl.glUseProgram(parentNode.getShaderProgram());
        else
            gl.glUseProgram(0);
    }



    /**
     * Turns the shader on and draws this (and children) nodes
     * @param gl
     */
    @Override
    public void draw(GL gl)
    {
        turnOnShader(gl);

        super.draw(gl);

        turnOffShader(gl);
    }
}
