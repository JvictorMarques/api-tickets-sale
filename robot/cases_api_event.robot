*** Settings ***
Resource    resource_api_event.robot
Resource    script_api_event.robot
Test Setup    Criar Sessao API

*** Test Cases ***
# --- 1. CENÁRIOS DE SUCESSO (STATUS 201) ---
#teste

TC-001: Cadastro com Campos Obrigatórios
    [Tags]    ${TAG_ALL_TESTS}    SUCESSO    MINIMO
    &{payload}=    Gerar Payload Sucesso Minimo
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_201}
    Validar Response Sucesso 201    ${response}    ${payload['name']}

TC-002: Cadastro com Todos os Campos
    [Tags]    ${TAG_ALL_TESTS}    SUCESSO    COMPLETO
    &{payload}=    Gerar Payload Sucesso Completo
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_201}
    Validar Response Sucesso 201    ${response}    ${payload['name']}   

TC-003: Falha ao preencher name massa de dados repetida
    [Tags]    ${TAG_ALL_TESTS}    SUCESSO    COMPLETO
    &{payload}=    Gerar Payload Sucesso Completo
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_409}
    Validar Response Sucesso 201    ${response}    ${payload['name']}

# --- 2. CENÁRIOS DE FALHA - CAMPOS OBRIGATÓRIOS AUSENTES (STATUS 400) ---

TC-004: Falha ao Não Preencher campo "name"
    [Tags]    ${TAG_ALL_TESTS}    FALHA    OBRIGATORIO    NAME
    &{payload}=    Gerar Payload Campo Ausente    name
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    "name" é obrigatório

TC-005: Falha ao Não Preencher campo "location"
    [Tags]    ${TAG_ALL_TESTS}    FALHA    OBRIGATORIO    LOCATION
    &{payload}=    Gerar Payload Campo Ausente    location
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    "location" é obrigatório

TC-006: Falha ao Não Preencher campo "dateInitial"
    [Tags]    ${TAG_ALL_TESTS}    FALHA    OBRIGATORIO    DATEINITIAL
    &{payload}=    Gerar Payload Campo Ausente    dateInitial
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    "dateInitial" é obrigatório

TC-007: Falha ao Não Preencher campo "dateFinal"
    [Tags]    ${TAG_ALL_TESTS}    FALHA    OBRIGATORIO    DATEFINAL
    &{payload}=    Gerar Payload Campo Ausente    dateFinal
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    "dateFinal" é obrigatório

TC-008: Falha com "capacity" Negativa (400)
    [Tags]    ${TAG_ALL_TESTS}    FALHA    CAPACITY
    &{payload}=    Gerar Payload Campo Negativo    capacity    -1
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    "capacity" deve ser maior que 0

TC-009: Falha com "ageRestriction" Negativa (400)
    [Tags]    ${TAG_ALL_TESTS}    FALHA    AGERESTRICTION
    &{payload}=    Gerar Payload Campo Negativo    ageRestriction    -1
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    "ageRestriction" não pode ser negativo


# --- 3. CENÁRIOS DE FALHA - REGRAS DE NEGÓCIO (STATUS 422) ---

TC-010: Falha com Data Final Anterior à Data Inicial
    [Tags]    ${TAG_ALL_TESTS}    FALHA    REGRA_DATA
    &{payload}=    Gerar Payload Data Final Anterior
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_422}
    # Validar Mensagem de Erro    ${response}    "dateFinal" não pode ser anterior à "dateInitial"

TC-011: Cadastro com Data Inicial Igual à Data Final
    [Tags]    ${TAG_ALL_TESTS}    SUCESSO    DATAS
    &{payload}=    Gerar Payload Datas Iguais
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_201}
    Validar Response Sucesso 201    ${response}    ${payload['name']}

TC-012: Falha com Data Inicial Já Transcorrida (Passada)
    [Tags]    ${TAG_ALL_TESTS}    FALHA    REGRA_DATA
    &{payload}=    Gerar Payload Data Passada
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    "dateInitial" deve ser presente ou futura

TC-013: Falha com Formato Inválido de Data
    [Tags]    ${TAG_ALL_TESTS}    FALHA    FORMATO
    &{payload}=    Gerar Payload Formato Data Invalido
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    Formato de data inválido


# --- 4. CENÁRIOS DE FALHA - TIPO DE DADO INVÁLIDO (STATUS 422) ---

TC-014: Falha com Tipo de Dado Inválido para "name" (Int)
    [Tags]    ${TAG_ALL_TESTS}    FALHA    TIPAGEM
    &{payload}=    Gerar Payload Tipo Invalido    int    ${exemplo_int}
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    "name" deve ser string

# TC-015: Falha com Tipo de Dado Inválido para "description" (Booleano)
#     [Tags]    ${TAG_ALL_TESTS}    FALHA    TIPAGEM
#     &{payload}=    Gerar Payload Tipo Invalido    description    ${TRUE}
#     ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_422}
    # Validar Mensagem de Erro    ${response}    "description" deve ser string

TC-016: Falha com Tipo de Dado Inválido para "capacity" (String)
    [Tags]    ${TAG_ALL_TESTS}    FALHA    TIPAGEM
    &{payload}=    Gerar Payload Tipo Invalido    capacity    capacidade_maxima
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    "capacity" deve ser um número inteiro

TC-017: Falha com Tipo de Dado Inválido para "ageRestriction" (String)
    [Tags]    ${TAG_ALL_TESTS}    FALHA    TIPAGEM
    &{payload}=    Gerar Payload Tipo Invalido    ageRestriction    maior_idade
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    "ageRestriction" deve ser um número inteiro

TC-018: Falha com Tipo de Dado Inválido para "dateInitial" (Número)
    [Tags]    ${TAG_ALL_TESTS}    FALHA    TIPAGEM
    &{payload}=    Gerar Payload Tipo Invalido    dateInitial    1672531200000
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    "dateInitial" deve ser string (datetime)

TC-019: Falha com Tipo de Dado Inválido para "dateFinal" (Número)
    [Tags]    ${TAG_ALL_TESTS}    FALHA    TIPAGEM
    &{payload}=    Gerar Payload Tipo Invalido    dateFinal    1672534800000
    ${response}=    Executar POST e Validar Status    ${payload}    ${STATUS_400}
    # Validar Mensagem de Erro    ${response}    "dateFinal" deve ser string (datetime)