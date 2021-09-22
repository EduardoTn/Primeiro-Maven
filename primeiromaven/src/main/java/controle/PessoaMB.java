package controle;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import entidade.EPessoa;
import util.PessoaDAO;
import util.SessionContext;

@Named("pessoaMB")
@SessionScoped
public class PessoaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	public PessoaMB() throws SQLException {

		if (SessionContext.getInstance().getAttribute("usuario") == null) {

			try {

				boolean respostaComprometida = FacesContext.getCurrentInstance().getExternalContext()
						.isResponseCommitted();

				if (!respostaComprometida) {
					FacesContext.getCurrentInstance().getExternalContext().redirect("Login.xhtml");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} 
		
		this.preencherCombo();
		this.preencherSexo();
		this.carregarLista();

	}

	@Inject
	private EPessoa pessoa;

	private List<EPessoa> listaPessoa = new ArrayList<>();
	private List<SelectItem> listaCombo = new ArrayList<SelectItem>();
	private List<SelectItem> listaSexo = new ArrayList<SelectItem>();

	public List<SelectItem> getListaSexo() {
		return listaSexo;
	}

	public void setListaSexo(List<SelectItem> listaSexo) {
		this.listaSexo = listaSexo;
	}

	MensagemMB oMsg = new MensagemMB();

	public EPessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(EPessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<EPessoa> getListaPessoa() {
		return listaPessoa;
	}

	public void setListaPessoa(List<EPessoa> listaPessoa) {
		this.listaPessoa = listaPessoa;
	}

	public List<SelectItem> getListaCombo() {
		return listaCombo;
	}

	public void setListaCombo(List<SelectItem> listaCombo) {
		this.listaCombo = listaCombo;
	}

	public void preencherCombo() {

		this.listaCombo.add(new SelectItem("0", "--Selecione--"));
		this.listaCombo.add(new SelectItem("Sistemas de Informação", "Sistemas de Informação"));
		this.listaCombo.add(new SelectItem("Química", "Química"));
		this.listaCombo.add(new SelectItem("Agronomia", "Agronomia"));
		this.listaCombo.add(new SelectItem("Zootecnia", "Zootecnia"));

	}
	public void preencherSexo() {

		this.listaSexo.add(new SelectItem("0", "--Selecione--"));
		this.listaSexo.add(new SelectItem("Masculino", "Masculino"));
		this.listaSexo.add(new SelectItem("Feminino", "Feminino"));

	}

//	public void salvar() throws ParseException {
//		this.listaPessoa.add(this.pessoa);
//		this.pessoa = new EPessoa();
//	}

	public void salvar() {
		PessoaDAO.getInstance().salvar(pessoa);
		this.carregarLista();
		limpar();
	}

	public void carregarLista() {
		listaPessoa.clear();
		listaPessoa = PessoaDAO.getInstance().listarTodos();
	}

	public void excluir() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		Object parametro = params.get("parametroIdent");
		pessoa.setId(Long.parseLong(parametro.toString()));
		PessoaDAO.getInstance().remover(pessoa);
		this.limpar();
		this.carregarLista();
	}

	public void alterar() {
		PessoaDAO.getInstance().alterar(pessoa);
		this.limpar();
		this.carregarLista();
	}

	public void limpar() {
		this.pessoa = new EPessoa();
		this.refresh();
	}

	public void prepararEdicao() throws ParseException {

		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		Object parametroId = params.get("parametroId");
		this.pessoa = PessoaDAO.getInstance().buscarPorID(Long.parseLong(parametroId.toString()));
		this.refresh();
	}

	public void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);
		context.renderResponse();
	}

}
